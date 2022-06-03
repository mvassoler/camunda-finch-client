package com.finch.camunda.client.configurations.locale;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Cria messageSource para i18n
 */
public abstract class MessageSourceFactory {

    /**
     * retorna um messageSource apontando para o path do parametro e setando as mensagens
     * como UTF-8
     * @param classpath path para as mensagens
     * @return
     */
    public static MessageSource createMessageSource(String classpath) {
        return MessageSourceFactory.createMessageSource(classpath, "UTF-8");
    }

    /**
     * retorna um messageSource apontando para o path do parametro e setando a codigificacao
     * passada por parametro
     * @param classpath path para as mensagens
     * @param encoding codificacao das mensagens
     * @return
     */
    public static MessageSource createMessageSource(String classpath, String encoding) {
        Locale.setDefault(new Locale("pt", "BR"));
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(classpath);
        messageSource.setDefaultEncoding(encoding);

        return messageSource;
    }
}
