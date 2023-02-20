package ru.kpfu.itis.shkalin.simplifytorrent.service.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterServiceImpl implements ConverterService {

    /**
    * For correct work, it is required that the name of fields of the Objects are equal
     */
    @Override
    public void update(Object updater, Object updatable) {

        Method[] updaterMethods = updater.getClass().getDeclaredMethods();
        Method[] updatableMethods = updatable.getClass().getDeclaredMethods();

        List<Method> settersList = Arrays.stream(updatableMethods)
                .filter(method -> method.getName().startsWith("set"))
                .collect(Collectors.toList());

        List<Method> gettersList = Arrays.stream(updaterMethods)
                .filter(method -> method.getName().startsWith("get"))
                .collect(Collectors.toList());

        String settersNameWithoutPrefix;
        String gettersNameWithoutPrefix;
        Object getterResult = null;

        for (Method setter : settersList) {
            settersNameWithoutPrefix = setter.getName().substring(3);

            for (Method getter : gettersList) {
                gettersNameWithoutPrefix = getter.getName().substring(3);

                if (settersNameWithoutPrefix.equals(gettersNameWithoutPrefix)) {
                    if (getter.getReturnType().getName().equals(setter.getParameterTypes()[0].getName())) {
                        try {
                            getterResult = getter.invoke(updater);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        try {
                            setter.invoke(updatable, getterResult);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
    }

}
