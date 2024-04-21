package cheolppochwippo.oe_mos_nae_mas_market.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SQSConfig {

	@Value("${aws.sqs.accessKey}")
	private String accessKey;

	@Value("${aws.sqs.secretKey}")
	private String secretKey;

	@Value("${aws.sqs.queue}")
	private String queueUrl;


	public void sendCouponMessages(List<String> phoneNumbers, String message) {
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
			.withRegion("ap-northeast-1")
			.withCredentials(
				new AWSStaticCredentialsProvider(
					new BasicAWSCredentials(
						accessKey,
						secretKey
					)
				)
			)
			.build();

		for (String phoneNumber : phoneNumbers) {
			Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
			messageAttributes.put("PhoneNumber",
				new MessageAttributeValue().withDataType("String").withStringValue("82"+phoneNumber));

			SendMessageRequest sendMessageRequest = new SendMessageRequest()
				.withQueueUrl(queueUrl)
				.withMessageBody(message)
				.withMessageAttributes(messageAttributes);

			sqs.sendMessage(sendMessageRequest);
		}

	}
}
