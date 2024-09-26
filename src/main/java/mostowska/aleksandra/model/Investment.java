package mostowska.aleksandra.model;

import lombok.*;
import mostowska.aleksandra.model.dto.investment.CreateInvestmentDto;
import mostowska.aleksandra.model.dto.investment.GetInvestmentDto;
import mostowska.aleksandra.model.utils.AssetType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Investment {
    private Long id;
    private AssetType assetType;
    private BigDecimal amountInvested;
    private BigDecimal currentValue;
    private LocalDateTime investmentDateTime;
    private Long userId;

    /**
     * Converts the `Investment` object to a `GetInvestmentDto`, which contains essential details about the investment.
     *
     * @return a new `GetInvestmentDto` instance representing this investment
     */
    public GetInvestmentDto toGetInvestmentDto() {
        return new GetInvestmentDto(id, assetType, amountInvested, currentValue);
    }

    /**
     * Checks if this investment belongs to a specific user by comparing user IDs.
     *
     * @param userIdToCheck the user ID to check against the investment's user ID
     * @return true if the user IDs match, false otherwise
     */
    public boolean hasUserId(Long userIdToCheck) {
        return Objects.equals(userIdToCheck, userId);
    }
}
