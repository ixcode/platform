package ixcode.platform.http.server.resource;

import ixcode.platform.http.template.TemplateEngine;

public class TemplatedPageMap {

    public static TemplatedPageMap pageMap(TemplateEngine templateEngine) {
        return new TemplatedPageMap(templateEngine);
    }

    private TemplatedPageMap(TemplateEngine templateEngine) {

    }



    public PageBuilder page(String pathMapping) {
        return null;
    }

    public static class PageBuilder {
        public RouteBuilder GET() {
            return null;
        }

        public RouteBuilder POST() {
            return null;
        }


        public TemplatedPageMap withNoData() {
            return null;
        }
    }

    public static class RouteBuilder {

        public FixedDataProvider withData() {
            return null;
        }

        public TemplatedPageMap withNoData() {
            return null;
        }

        public TemplatedPageMap redirectTo(String pathMapping) {
            return null;
        }

        public RouteBuilder processRequestWith(PostHandler postHandler) {
            return null;
        }

        public TemplatedPageMap withDataFrom(DataProvider dataProvider) {
            return null;
        }

        public TemplatedPageMap redirectToSelf(String s) {

            return null;
        }

        public PageBuilder redirectToChildPage(String childPath) {
            return null;
        }
    }

    public static class FixedDataProvider {

        public FixedDataProvider key(String key) {
            return null;
        }

        public FixedDataProvider value(String value) {
            return null;
        }



        public PageBuilder map() {
            return null;
        }
    }
}
