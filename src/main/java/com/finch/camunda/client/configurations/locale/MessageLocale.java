package com.finch.camunda.client.configurations.locale;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageLocale implements MessageLocaleComponent {


    /**
     * i18n Business
     * Método para atribuição de mensagens customizadas de negócio
     *
     * @return messageSource
     */
    public String businessMessageSource(String keyMessage, Object... objects) {
        MessageSource messageSource = MessageSourceFactory.createMessageSource("classpath:/messages/business/message");
        return getMessage(messageSource, keyMessage, objects, Locale.getDefault());
    }

    /**
     * i18n Validation
     * Método para atribuição de mensagens customizadas de validação
     *
     * @return messageSource
     */
    public String validationMessageSource(String keyMessage, Object... objects) {
        MessageSource messageSource = MessageSourceFactory.createMessageSource("classpath:/messages/validation/message");
        return getMessage(messageSource, keyMessage, objects);
    }


    /**
     * i18n Bean
     * Método para atribuição de mensagens customizadas de classes
     *
     * @return messageSource
     */
    public String beanMessageMessageSource(String keyMessage, Object... objects) {
        MessageSource messageSource = MessageSourceFactory.createMessageSource("classpath:/messages/bean/message");
        return messageSource.getMessage(keyMessage, objects, Locale.getDefault());
    }

    private String getMessage (final MessageSource messageSource, final String key, final Object... params) {
        if (key == null) {
            log.error("Messages Key can't be null!");
            return null;
        }

        final String msg;
        if (params != null && params.length > 0) {
            msg = messageSource.getMessage(key.replaceAll("[{}]", ""), params, Locale.getDefault());
        } else {
            msg = messageSource.getMessage(key.replaceAll("[{}]", ""), (Object[]) null, Locale.getDefault());
        }
        if (key.equals(msg)) {
            log.error("Messages Key wasn't found! KEY: " + key);
        }
        return msg;
    }
}
