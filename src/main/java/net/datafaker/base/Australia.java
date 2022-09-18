package net.datafaker.base;

/**
 * @since 1.2.0
 */
public class Australia extends AbstractProvider<BaseProviders> {

    protected Australia(BaseFaker faker) {
        super(faker);
    }

    public String locations() {
        return resolve("australia.locations");
    }

    public String animals() {
        return resolve("australia.animals");
    }

    public String states() {
        return resolve("australia.states");
    }

}
