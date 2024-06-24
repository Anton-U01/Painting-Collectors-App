package com.paintingscollectors.init;

import com.paintingscollectors.model.entity.Style;
import com.paintingscollectors.model.entity.StyleEnum;
import com.paintingscollectors.repository.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StyleDescriptionInit implements CommandLineRunner {
    private final StyleRepository styleRepository;
    private final Map<StyleEnum,String> stylesMap = Map.of(
      StyleEnum.ABSTRACT,"Abstract art does not attempt to represent recognizable subjects in a realistic manner. ",
      StyleEnum.IMPRESSIONISM,"Impressionism is a painting style most commonly associated with the 19th century where small brush strokes are used to build up a larger picture.",
      StyleEnum.EXPRESSIONISM,"Expressionism is a style of art that doesn't concern itself with realism.",
      StyleEnum.REALISM,"Also known as naturalism, this style of art is considered as 'real art' and has been the dominant style of painting since the Renaissance." ,
      StyleEnum.SURREALISM,"Surrealism is characterized by dreamlike, fantastical imagery that often defies logical explanation."
    );

    public StyleDescriptionInit(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(styleRepository.count() > 0){
            return;
        }
        List<Style> styleList = Arrays.stream(StyleEnum.values())
                .map(s -> new Style(s,stylesMap.get(s)))
                .collect(Collectors.toList());
        this.styleRepository.saveAll(styleList);
    }
}
