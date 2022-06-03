package com.finch.camunda.client.configurations.locale;

/**
 * Classe de Serviço (Componente) das mensagens de validação e regras de negócio
 *
 * @author neto.diegues
 * @version 1.0.0
 * @since 11/12/2019
 */
public interface MessageLocaleComponent {


    /**
     * i18n Business
     * Método para atribuição de mensagens customizadas de negócio
     *
     * @return messageSource
     */
    String businessMessageSource(String keyMessage, Object... objects);

    /**
     * i18n Validation
     * Método para atribuição de mensagens customizadas de validação
     *
     * @return messageSource
     */
    String validationMessageSource(String keyMessage, Object... objects);


    /**
     * i18n Bean
     * Método para atribuição de mensagens customizadas de classes
     *
     * @return messageSource
     */
    String beanMessageMessageSource(String keyMessage, Object... objects);
}
