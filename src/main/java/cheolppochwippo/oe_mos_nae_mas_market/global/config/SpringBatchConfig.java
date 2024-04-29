//package cheolppochwippo.oe_mos_nae_mas_market.global.config;
//
//import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
//import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class SpringBatchConfig {
//	private final OrderRepository orderRepository;
//	private final JobLauncher jobLauncher;
//	private final JobRepository jobRepository;
//	private final PlatformTransactionManager platformTransactionManager;
//
//	@Bean
//	public Job simpleJob1(Step simpleStep1) {
//		return new JobBuilder("simpleJob", jobRepository)
//			.start(simpleStep1)
//			.build();
//	}
//
//	@Bean
//	public Step simpleStep1(Tasklet testTasklet) {
//		return new StepBuilder("simpleStep1", jobRepository)
//			.tasklet(testTasklet, platformTransactionManager)
//			.build();
//	}
//
//	@Bean
//	public Tasklet testTasklet() {
//		return ((contribution, chunkContext) -> {
//			Order order =orderRepository.findById(1L).get();
//
//			return RepeatStatus.FINISHED;
//		});
//	}
//	@Scheduled(cron = "0 05 16 * * *")
//	@JobScope
//	public void runBatchJob() throws Exception {
//		JobParameters jobParameters = new JobParametersBuilder()
//			.addLong("time", System.currentTimeMillis())
//			.toJobParameters();
//		jobLauncher.run(simpleJob1(simpleStep1(testTasklet())), jobParameters);
//	}
//}
