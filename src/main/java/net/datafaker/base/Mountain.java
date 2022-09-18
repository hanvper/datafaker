package net.datafaker.base;

/**
 * A generator for Mountain names and ranges.
 *
 * @since 1.1.0
 */
public class Mountain extends AbstractProvider<BaseProviders> {

    protected Mountain(BaseFaker faker) {
        super(faker);
    }

    public String name() {
        return resolve("mountain.name");
    }

    public String range() {
        return resolve("mountain.range");
    }
}
