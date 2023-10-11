package com.lucasdc.demoresttemplate;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lucasdc.demoresttemplate.consultacep.CepResultDTO;
import com.lucasdc.demoresttemplate.consultacep.ConsultaCepApi;

public class TelegramBot extends TelegramLongPollingBot {

   private ConsultaCepApi consultaCepApi = new ConsultaCepApi();

   @Override
   public void onUpdateReceived(Update update) {
       if (update.hasMessage() && update.getMessage().hasText()) {
           Message message = update.getMessage();
           String text = message.getText();
   
           if (text.matches("\\d{8}")) {
               // Se a mensagem for um CEP válido (8 dígitos numéricos), faça a consulta e retorne o resultado.
               String cep = text;
               CepResultDTO resultadoCep = consultaCepApi.consultaCep(cep);
               
               if (resultadoCep != null) {
                   String resposta = "Resultado da consulta para o CEP " + cep + ":\n"
                           + "CEP: " + resultadoCep.getCep() + "\n"
                           + "Logradouro: " + resultadoCep.getLogradouro() + "\n"
                           + "Complemento: " + resultadoCep.getComplemento() + "\n"
                           + "Bairro: " + resultadoCep.getBairro() + "\n"
                           + "Localidade: " + resultadoCep.getLocalidade() + "\n"
                           + "UF: " + resultadoCep.getUf() + "\n"
                           + "IBGE: " + resultadoCep.getIbge() + "\n"
                           + "GIA: " + resultadoCep.getGia() + "\n"
                           + "DDD: " + resultadoCep.getDdd() + "\n"
                           + "SIAFI: " + resultadoCep.getSiafi();
       
                   // Envie a resposta de volta ao usuário.
                   SendMessage sendMessage = new SendMessage();
                   sendMessage.setChatId(message.getChatId().toString());
                   sendMessage.setText(resposta);
       
                   try {
                       execute(sendMessage);
                   } catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
   }

   @Override
   public String getBotToken() {
       return System.getenv("TELEGRAM_BOT_TOKEN");     
   }
   

    @Override
    public String getBotUsername() {
        return "tcc_teste_bot";
    }
    
}
