package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalOrderNameDto {

	private Long sum;

	private Long count;

	private String productName;

	private Long quantity;


	public static TotalOrderNameDto fromTuple(Tuple tuple) {
		Long sum = tuple.get(QOrder.order.price.sum());
		Long count = tuple.get(QOrder.order.count());
		String productName = tuple.get(QOrder.order.product.productName);
		Long quantity = tuple.get(QOrder.order.quantity);
		return new TotalOrderNameDto(sum, count, productName, quantity);
	}

}
