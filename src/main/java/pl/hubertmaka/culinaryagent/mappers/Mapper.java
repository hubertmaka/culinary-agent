package pl.hubertmaka.culinaryagent.mappers;

/**
 * Generic interface for mapping between two types.
 *
 * @param <T> the target type
 * @param <S> the source type
 */
public interface Mapper<T, S> {
    /**
     * Maps an object of type S to an object of type T.
     *
     * @param source the source object to be mapped
     * @return the mapped object of type T
     */
    public T mapFrom(S source);
    /**
     * Maps an object of type T to an object of type S.
     *
     * @param target the target object to be mapped
     * @return the mapped object of type S
     */
    public S mapTo(T target);
}
