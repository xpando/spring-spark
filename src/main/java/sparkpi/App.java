package sparkpi;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class App implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) {
		final SparkSession spark = SparkSession.builder()
			.appName("sparkpi")
			.master("local[*]")
			.getOrCreate();

		final JavaSparkContext sc = JavaSparkContext.fromSparkContext(spark.sparkContext());

		final int slices = args.length >= 1 ? Integer.parseInt(args[0]) : 2;
		final int n = (100000L * slices) > Integer.MAX_VALUE ? Integer.MAX_VALUE : 100000 * slices;

		final List<Integer> xs = IntStream.rangeClosed(0, n).boxed().collect(Collectors.toList());
		final JavaRDD<Integer> dataSet = sc.parallelize(xs, slices);

		final JavaRDD<Integer> pointsInsideTheCircle = dataSet.map(integer -> {
			double x = Math.random() * 2 - 1;
			double y = Math.random() * 2 - 1;
			return (x * x + y * y ) < 1 ? 1: 0;
		});

		final int count = pointsInsideTheCircle.reduce(Integer::sum);
		final double pi = 4f * count / n;

		log.info("Pi was estimated as:" + pi);

		spark.stop();
	}
}
