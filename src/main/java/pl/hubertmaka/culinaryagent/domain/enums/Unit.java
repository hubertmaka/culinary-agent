package pl.hubertmaka.culinaryagent.domain.enums;

public enum Unit {
    GRAM("g"),
    KILOGRAM("kg"),
    LITER("l"),
    MILLILITER("ml"),
    TEASPOON("tsp"),
    TABLESPOON("tbsp"),
    CUP("cup"),
    PIECE("piece");

    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
