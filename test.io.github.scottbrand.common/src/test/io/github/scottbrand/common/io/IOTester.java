package test.io.github.scottbrand.common.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.github.scottbrand.common.ServiceLocator;
import io.github.scottbrand.common.Strings;

@Component
public class IOTester
{
	@Activate
	public void activate(ComponentContext ctx)
	{
		//Path path = Paths.get("files", "sample2.txt");
		Path path = Paths.get("files", "sample3.txt");
		ServiceLocator.getLogger().debug("processing file: " + path.toFile().getAbsolutePath());
		//ParsingConsumer parsingConsumer = new ParsingConsumer();
		try
		{
			URL url = ctx.getBundleContext().getBundle().getResource("files/sample3.txt");  //10165.CCA.20150122.1.txt.ansi.gbif");			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), Charset.forName("UTF-8")));
			String line = null;
			long ln = 1L;
			while ((line = br.readLine()) != null)
			{
			    ServiceLocator.getLogger().debug(ln++ + ":" + line);
			}
			br.close();
			ServiceLocator.getLogger().debug("DONE");
			//FixedBatchSpliterator<String> iter = new FixedBatchSpliterator(Files.lines(path,StandardCharsets.UTF_8), 10);
			//FixedBatchSpliterator<String> iter = new FixedBatchSpliterator(Files.lines(path,StandardCharsets.UTF_8), 50);
			//FixedBatchSpliterator<String> iter = new FixedBatchSpliterator(Files.lines(path,StandardCharsets.ISO_8859_1), 5000);
			
			//Stream<String> strings = FixedBatchSpliterator.withBatchSize(Files.lines(path,StandardCharsets.UTF_8), 10);
			//try (Stream<String> lines = strings)
			
			
			//try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8))   //FixedBatchSpliterator.withBatchSize(Files.lines(path,StandardCharsets.UTF_8), 5000))

//			try (Stream<String> lines = iter.getStream())
//			{
//				lines.parallel().forEach(parsingConsumer);
//				ServiceLocator.getLogger().debug("lines: " + iter.getRecordsRead());
//			}
//			catch (Throwable t)
//			{
//			    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
//			}
//			ServiceLocator.getLogger().debug("after try, lines: " + iter.getRecordsRead());
		}
		catch (Throwable t)
		{
		    ServiceLocator.getLogger().error(Strings.EXCEPTION,t);
		}
	}
	
	
	class ParsingConsumer implements Consumer<String>
	{

		AtomicLong atomicLong = new AtomicLong(0L);
		
		@Override
		public void accept(String t)
		{
		    ServiceLocator.getLogger().debug(atomicLong.incrementAndGet() + ": " + t);
		}
		
		
		
		
		
	}
	
}
