package ixcode.platform.http.representation;

public class VanillaHypermedia extends HypermediaResourceBuilder<VanillaHypermedia> {

    public static VanillaHypermedia hypermedia(String... types) {
        return new VanillaHypermedia(types);
    }

    private VanillaHypermedia(String[] types) {
        super(types);
    }


}