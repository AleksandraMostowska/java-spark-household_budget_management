package mostowska.aleksandra.model.dto.investment;

import mostowska.aleksandra.model.utils.AssetType;

/**
 * A Data Transfer Object (DTO) used to represent available investment options.
 * It includes details about an investment asset type and a description of that asset.
 *
 * @param id The unique identifier for the available investment.
 * @param assetType The type of the asset available for investment (e.g., GOLD, STOCK, CRYPTOCURRENCY, etc.).
 * @param description A brief description of the available investment.
 */
public record GetAvailableInvestmentDto (Long id, AssetType assetType, String description) {

    /**
     * Parses and formats the description of the available investment based on its asset type.
     *
     * @return A new GetAvailableInvestmentDto object with a formatted description.
     */
    public GetAvailableInvestmentDto parseAvailableInvestmentsDto() {
        return new GetAvailableInvestmentDto(id, assetType, parseDescription(assetType, description));
    }

    /**
     * Parses the description based on the asset type.
     * For example, it formats details like price per ounce for GOLD or price per share for STOCK.
     *
     * @param assetType The type of the asset.
     * @param description The original description of the asset.
     * @return A formatted description based on the asset type.
     */
    private String parseDescription(AssetType assetType, String description) {
        String[] parts = description.split(";");

        return switch (assetType) {
            case GOLD -> String.format("Price per ounce: %s, Interest: %s", parts[0].trim(), parts[2].trim());
            case STOCK -> String.format("Price per share: %s, Dividends: %s", parts[0].trim(), parts[2].trim());
            case BONDS -> String.format("Bond Price: %s, Annual Yield: %s", parts[0].trim(), parts[2].trim());
            case REAL_ESTATE -> String.format("Property Price: %s, Rental Yield: %s", parts[0].trim(), parts[2].trim());
            case CRYPTOCURRENCY -> String.format("Price per Bitcoin: %s, Volatility: %s", parts[0].trim(), parts[2].trim());
            case COMMODITIES -> String.format("Price per barrel: %s, Price Trend: %s", parts[0].trim(), parts[2].trim());
            case OTHER -> String.format("Description: %s", parts[0].trim());
        };
    }
}
