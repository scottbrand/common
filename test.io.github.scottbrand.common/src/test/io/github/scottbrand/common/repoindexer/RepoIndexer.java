package test.io.github.scottbrand.common.repoindexer;

import org.osgi.service.component.annotations.Component;



@Component
public class RepoIndexer
{
//	private volatile ResourceIndexer	indexer;
//
//	private String						REPO_ROOT	= "C:\\programs\\other\\r5-repos";
//	private String[]					repoDirs	= { REPO_ROOT + "\\osgi-base", REPO_ROOT + "\\eclipse4.4", REPO_ROOT + "\\felix4.4", REPO_ROOT + "\\gpd-depends", REPO_ROOT + "\\gpd", REPO_ROOT + "\\gpd-ws" };
//
//	private String[]					repoNames	= { "OSGi-Base", "Eclipse4.4", "Felix4.4", "GPD-Dependencies", "GPD", "GPD-WS" };
//
//
//
//
//
//	@Activate
//	public void activate(ComponentContext ctx)
//	{
//	    ServiceLocator.getLogger().debug("Here");
//	    ServiceLocator.getLogger().debug("Indexer is" + indexer);
//		for (int i = 0; i < repoDirs.length; i++)
//			buildIndex(new File(repoDirs[i]), repoNames[i]);
//
//		// setupWatcher();
//	}
//
//
//
//
//
//	@Reference
//	public void set(ResourceIndexer indexer)
//	{
//		this.indexer = indexer;
//	}
//
//
//
//
//
//
//	private void buildIndex(File rootDir, String repoName)
//	{
//	    ServiceLocator.getLogger().debug("buildingIndex");
//		// File dir = new File(REPO_ROOT);
//		File[] files = rootDir.listFiles(new FilenameFilter()
//		{
//
//			@Override
//			public boolean accept(File arg0, String arg1)
//			{
//				return arg1.endsWith(".jar");
//			}
//		});
//
//		if (files == null || files.length == 0)
//		{
//		    ServiceLocator.getLogger().debug("files are empty");
//			return;
//		}
//		Set<File> fileSet = new HashSet<File>();
//		for (File f : files)
//			fileSet.add(f);
//
//		Map<String, String> map = new HashMap<String, String>();
//
//		map.put(ResourceIndexer.ROOT_URL, rootDir.getAbsolutePath());
//		map.put(ResourceIndexer.COMPRESSED, "true");
//		map.put(ResourceIndexer.PRETTY, "true");
//		map.put(ResourceIndexer.REPOSITORY_NAME, repoName);
//		try
//		{
//			FileOutputStream pw = new FileOutputStream(rootDir.getAbsolutePath() + File.separator + "index.xml.gz");
//			indexer.index(fileSet, pw, map);
//			ServiceLocator.getLogger().debug("Index was built");
//		}
//		catch (Exception e)
//		{
//		    ServiceLocator.getLogger().error(Strings.EXCEPTION,e);
//		}
//	}
}
