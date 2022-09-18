package net.datafaker.base;

/**
 * @since 0.8.0
 */
public class Matz extends AbstractProvider<BaseProviders> {

    protected Matz(final BaseFaker faker) {
        super(faker);
    }

    public String quote() {
        return resolve("matz.quotes");
    }
}
