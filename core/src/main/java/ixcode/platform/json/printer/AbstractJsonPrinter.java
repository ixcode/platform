package ixcode.platform.json.printer;

public abstract class AbstractJsonPrinter implements JsonPrinter, PrintSource {

    @Override public PrintTargetBuilder print(Object root) {
        return new PrintTargetBuilder(this, root);
    }

}