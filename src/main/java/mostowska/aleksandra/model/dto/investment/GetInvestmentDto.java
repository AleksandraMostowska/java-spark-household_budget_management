package mostowska.aleksandra.model.dto.investment;

import mostowska.aleksandra.model.utils.AssetType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) for representing investment details.
 * It encapsulates basic information about an investment, including the asset type, amount invested, and current value.
 *
 * @param id The unique identifier for the investment.
 * @param assetType The type of the asset (e.g., GOLD, STOCK, BONDS, etc.).
 * @param amountInvested The amount of money that was originally invested.
 * @param currentValue The current market value of the investment.
 */
public record GetInvestmentDto(Long id, AssetType assetType, BigDecimal amountInvested, BigDecimal currentValue) {
}
