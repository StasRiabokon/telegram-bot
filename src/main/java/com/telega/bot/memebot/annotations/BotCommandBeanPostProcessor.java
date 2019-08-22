package com.telega.bot.memebot.annotations;

import com.telega.bot.memebot.commands.interfaces.Command;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotCommandBeanPostProcessor implements BeanPostProcessor {

    public static final List<Command> COMMANDS = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BotCommand.class)) {
            COMMANDS.add((Command) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
