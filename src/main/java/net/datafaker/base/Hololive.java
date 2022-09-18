package net.datafaker.base;

/**
 * @since 1.5.0
 */
public class Hololive extends AbstractProvider<BaseProviders> {

    protected Hololive(BaseFaker faker) {
        super(faker);
    }

    public String talent() {
        return resolve("hololive.talent");
    }
}
