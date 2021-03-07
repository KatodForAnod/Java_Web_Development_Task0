package by.makarchuk.infhandling.parser;

import by.makarchuk.infhandling.composite.TextComposite;
import by.makarchuk.infhandling.composite.TypeComponent;
import org.apache.log4j.Logger;

public class TextParser extends AbstractTextParser {
    private static final Logger logger = Logger.getLogger(SentenceParse.class);
    private static final String PARAGRAPH_SPLIT ="\n";
    private AbstractTextParser paragraphParser = new ParagraphParser();

    @Override
    public TextComposite parse(String text) {
        logger.info("parse text: " + text);
        TextComposite textComposite = new TextComposite(TypeComponent.TEXT);
        for(String paragraphText : text.split(PARAGRAPH_SPLIT)) {
            TextComposite paragraph = paragraphParser.parse(paragraphText);
            textComposite.addComponent(paragraph);
        }

        return textComposite;
    }
}