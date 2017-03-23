package io.github.scottbrand.common.sed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

import io.github.scottbrand.common.Patterns;

public class Sed
{
	public int sed(File input, File output, Map<String, String> args)

	{
		return sed(input, output, args, true);
	}





	public int sed(File input, File output, Map<String, String> args, boolean failOnNoMatch)
	{
		FileReader fr = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		FileWriter fw = null;

		Patterns pr = new Patterns();
		int count = 0;
		try
		{
			fr = new FileReader(input);
			br = new BufferedReader(fr);

			fw = new FileWriter(output);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);

			String s = null;
			while ((s = br.readLine()) != null)
			{
				pw.print(pr.doReplace(s, args, failOnNoMatch));
				pw.print("\n");
				count = count + pr.getReplacementCount();
			}
			return count;
		}
		catch (Throwable t)
		{
			return -1;
		}
		finally
		{
			if (pw != null)
				pw.close();
			if (bw != null)
				try
				{
					bw.close();
				}
				catch (Throwable t)
				{
				}
			;
			if (fw != null)
				try
				{
					fw.close();
				}
				catch (Throwable t)
				{
				}
			;
			if (br != null)
				try
				{
					br.close();
				}
				catch (Throwable t)
				{
				}
			;
			if (fr != null)
				try
				{
					fr.close();
				}
				catch (Throwable t)
				{
				}
			;
		}
	}

}

/*
 * public class Sed { final File file; final Replacer macro; File output;
 * boolean backup = true;
 * 
 * final Map<Pattern, String> replacements = new LinkedHashMap<Pattern,
 * String>();
 * 
 * 
 * 
 * 
 * 
 * public Sed(Replacer macro, File file) { assert file.isFile(); this.file =
 * file; this.macro = macro; }
 * 
 * 
 * 
 * 
 * 
 * public Sed(File file) { assert file.isFile(); this.file = file; this.macro =
 * null; }
 * 
 * 
 * 
 * 
 * 
 * public void setOutput(File f) { output = f; }
 * 
 * 
 * 
 * 
 * 
 * public void replace(String pattern, String replacement) {
 * replacements.put(Pattern.compile(pattern), replacement); }
 * 
 * 
 * 
 * 
 * 
 * public int doIt() throws IOException { int actions = 0; BufferedReader brdr =
 * new BufferedReader(new InputStreamReader(new FileInputStream(file),
 * "UTF-8")); File out; if (output != null) out = output; else out = new
 * File(file.getAbsolutePath() + ".tmp"); PrintWriter pw = new PrintWriter(new
 * OutputStreamWriter(new FileOutputStream(out), "UTF-8")); try { String line;
 * while ((line = brdr.readLine()) != null) { for (Pattern p :
 * replacements.keySet()) { try { String replace = replacements.get(p); Matcher
 * m = p.matcher(line);
 * 
 * StringBuffer sb = new StringBuffer(); while (m.find()) { String tmp =
 * setReferences(m, replace); if (macro != null) tmp =
 * Matcher.quoteReplacement(macro.process(tmp)); m.appendReplacement(sb, tmp);
 * actions++; } m.appendTail(sb);
 * 
 * line = sb.toString(); } catch (Exception e) { throw new IOException("where: "
 * + line + ", pattern: " + p.pattern() + ": " + e.getMessage()); } }
 * pw.println(line); } } finally { brdr.close(); pw.close(); }
 * 
 * if (output == null) { if (backup) { File bak = new
 * File(file.getAbsolutePath() + ".bak"); IO.rename(file, bak); } IO.rename(out,
 * file); }
 * 
 * return actions; }
 * 
 * 
 * 
 * 
 * 
 * private String setReferences(Matcher m, String replace) { StringBuilder sb =
 * new StringBuilder(); for (int i = 0; i < replace.length(); i++) { char c =
 * replace.charAt(i); if (c == '$' && i < replace.length() - 1 &&
 * Character.isDigit(replace.charAt(i + 1))) { int n = replace.charAt(i + 1) -
 * '0'; if (n <= m.groupCount()) sb.append(m.group(n)); i++; } else
 * sb.append(c); } return sb.toString(); }
 * 
 * 
 * 
 * 
 * 
 * public void setBackup(boolean b) { this.backup = b; } }
 */