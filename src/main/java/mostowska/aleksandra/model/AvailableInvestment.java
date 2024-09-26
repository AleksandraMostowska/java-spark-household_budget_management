package mostowska.aleksandra.model;

import lombok.*;
import mostowska.aleksandra.model.dto.investment.GetAvailableInvestmentDto;
import mostowska.aleksandra.model.utils.AssetType;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableInvestment {
    private Long id;
    private AssetType assetType;
    private String description;

    /**
     * Converts the `AvailableInvestment` object to a `GetAvailableInvestmentDto`, which contains details about
     * the available investment and parses it to return a structured DTO representation.
     *
     * @return a `GetAvailableInvestmentDto` instance representing this available investment
     */
    public GetAvailableInvestmentDto toGetAvailableInvestmentDto() {
        return new GetAvailableInvestmentDto(id, assetType, description).parseAvailableInvestmentsDto();
    }

    /**
     * Compares the asset type of this available investment with the asset type of the provided investment.
     *
     * @param investment the investment to compare the asset type against
     * @return true if the asset types match, false otherwise
     */
    public boolean isAssetType(Investment investment) {
        return assetType.equals(investment.toGetInvestmentDto().assetType());
    }
}
