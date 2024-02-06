package com.example.banksystem.utils;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Model mapper utils.
 */
public class ModelMapperUtils {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private static final Converter<OffsetDateTime, Long> OFFSET_DATE_TIME_LONG_CONVERTER = new AbstractConverter<>() {
        protected Long convert(OffsetDateTime source) {
            return source == null ? null : source.toEpochSecond();
        }
    };

    private static void defaultConfig(MatchingStrategy strategy) {
        MODEL_MAPPER.getConfiguration().setMatchingStrategy(strategy);
        MODEL_MAPPER.getConfiguration().setSkipNullEnabled(true);
        MODEL_MAPPER.addConverter(OFFSET_DATE_TIME_LONG_CONVERTER);
    }

    private static <T, D> void defaultConfigWithCondition(MatchingStrategy strategy, Condition<T, D> condition) {
        defaultConfig(strategy);
        MODEL_MAPPER.getConfiguration().setPropertyCondition(condition);
    }

    /**
     * Map d with strategy option.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param entity      the entity
     * @param destination the destination
     * @param strategy    strategy
     * @return the d
     */
    public static <T, D> D map(T entity, Class<D> destination, MatchingStrategy strategy) {
        defaultConfig(strategy);
        return MODEL_MAPPER.map(entity, destination);
    }

    /**
     * Map d.
     *
     * @param <T>      the type parameter
     * @param <D>      the type parameter
     * @param entity   the entity
     * @param type     the type
     * @param strategy the strategy
     * @return the d
     */
    public static <T, D> D map(T entity, Type type, MatchingStrategy strategy) {
        defaultConfig(strategy);
        return MODEL_MAPPER.map(entity, type);
    }

    /**
     * Map.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param source      the source
     * @param destination the destination
     * @param strategy    the strategy
     */
    public static <T, D> void map(T source, T destination, MatchingStrategy strategy) {
        defaultConfig(strategy);
        MODEL_MAPPER.map(source, destination);
    }

    /**
     * Map d.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param entity      the entity
     * @param destination the destination
     * @return the d
     */
    public static <T, D> D map(T entity, Class<D> destination) {
        if (entity == null) {
            return null;
        }
        return map(entity, destination, MatchingStrategies.STRICT);
    }

    /**
     * Map d.
     *
     * @param <T>    the type parameter
     * @param <D>    the type parameter
     * @param entity the entity
     * @param type   the type
     * @return the d
     */
    public static <T, D> D map(T entity, Type type) {
        return map(entity, type, MatchingStrategies.STRICT);
    }

    /**
     * Map.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param source      the source
     * @param destination the destination
     */
    public static <T, D> void map(T source, T destination) {
        map(source, destination, MatchingStrategies.STRICT);
    }

    /**
     * Map all list with strategy option.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param sources     the sources
     * @param destination the destination
     * @param strategy    strategy
     * @return the list
     */
    public static <T, D> List<D> mapAll(List<T> sources, Class<D> destination, MatchingStrategy strategy) {
        defaultConfig(strategy);
        return sources.stream()
                .map(el -> MODEL_MAPPER.map(el, destination))
                .collect(Collectors.toList());
    }

    /**
     * Map all list.
     * <p>
     * К использованию больше рекомендован {@link #map(Object, Type)}, который принимает generic типы.
     *
     * @param <T>         the type parameter
     * @param <D>         the type parameter
     * @param sources     the sources
     * @param destination the destination
     * @return the list
     */
    public static <T, D> List<D> mapAll(List<T> sources, Class<D> destination) {
        return mapAll(sources, destination, MatchingStrategies.STRICT);
    }

    /**
     * Маппинг исходного объекта на результирующий с стратегий маппинга и условием.
     *
     * @param <T>               the type parameter
     * @param <D>               the type parameter
     * @param entity            the entity
     * @param destinationEntity the destination entity
     * @param strategy          the strategy
     * @param condition         the condition
     */
    public static <T, D> void defaultMapWIthCondition(T entity, D destinationEntity, MatchingStrategy strategy, Condition<?, ?> condition) {
        defaultConfigWithCondition(strategy, condition);
        MODEL_MAPPER.map(entity, destinationEntity);
    }

    /**
     * Маппинг исходного объекта на результирующий с условием.
     *
     * @param <T>               the type parameter
     * @param <D>               the type parameter
     * @param entity            the entity
     * @param destinationEntity the destination entity
     * @param condition         the condition
     */
    public static <T, D> void defaultMapWIthCondition(T entity, D destinationEntity, Condition<?, ?> condition) {
        if (entity == null) {
            return;
        }
        defaultMapWIthCondition(entity, destinationEntity, MatchingStrategies.STRICT, condition);
    }
}
