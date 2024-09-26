package mostowska.aleksandra.model.dto.investment;

import mostowska.aleksandra.model.Investment;
import mostowska.aleksandra.model.utils.AssetType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * A Data Transfer Object (DTO) used for creating new investment entries.
 * It carries essential information required to create an investment.
 *
 * @param assetType The type of asset being invested in.
 * @param amountInvested The amount of money being invested.
 * @param investmentDateTime The date and time when the investment was made.
 */
public record CreateInvestmentDto(
        AssetType assetType,
        BigDecimal amountInvested,
        LocalDateTime investmentDateTime
) {

    /**
     * Converts the CreateInvestmentDto into an Investment entity.
     *
     * @param userId The ID of the user making the investment.
     * @return A new Investment object created from this DTO.
     */
    public Investment toInvestment(Long userId) {
        return Investment
                .builder()
                .assetType(assetType)
                .amountInvested(amountInvested)
                .currentValue(amountInvested)
                .investmentDateTime(investmentDateTime)
                .userId(userId)
                .build();
    }

    /**
     * Validates whether the assetType provided in the DTO is valid.
     *
     * @return true if the assetType is valid; false otherwise.
     */
    public boolean isAssetType() {
        return Arrays.stream(AssetType.values())
                .anyMatch(assetType -> assetType.equals(assetType()));
    }
}
