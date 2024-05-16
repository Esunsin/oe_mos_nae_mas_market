package cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductImageRepositoryJdbc {

    public void productImageBulkInsert(List<ProductImage> productImages) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/omnmm", "root", "Jinchan0312!@");

            // 쿼리 작성
            String query = "INSERT INTO Image (dtype, url, product_id) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);

            // 벌크 삽입 처리
            for (ProductImage productImage : productImages) {
                statement.setString(1, "P");
                statement.setString(2, productImage.getUrl());
                statement.setLong(3, productImage.getProduct().getId());
                statement.addBatch();
            }

            // 배치 실행
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
