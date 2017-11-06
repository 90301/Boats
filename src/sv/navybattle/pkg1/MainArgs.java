package sv.navybattle.pkg1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainArgs {
	
	static boolean errorExit = true;
	
	public static void handle(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			try {
				String vstr = null;
				boolean v = false;
				
				switch (arg) {
				
				case "--help":
					if (++i < args.length) {
						help(args[i]);
					} else {
						help();
					}
					break;
				case "-h": case "-?":
					help();
					break;
					
				case "-e": case "--errorExit":
					errorExit = true;
					break;
				case "-E": case "--errorWarn":
					errorExit = false;
					break;
					
				case "-d": case "--delay": case "--simSpeed":
					Main.simSpeed = Integer.parseInt(args[++i]);
					break;
				case "-D": case "--nodelay":
					Main.simSpeed = 0;
					break;
					
				case "-t": case "--teams":
					Main.teams = Integer.parseInt(args[++i]);
					break;
					
				case "-n": case "--ships": case "--initialShips":
					Main.initialShips = Integer.parseInt(args[++i]);
					break;
					
				case "--runs": case "--simsToRun":
					Main.simsToRun = Integer.parseInt(args[++i]);
					break;
				case "-r": case "--repeat":
					Main.simsToRun = -1;
					break;
				case "-R": case "--norepeat":
					Main.simsToRun = 1;
					break;
					
				case "--shipsPlus":
					Main.shipsPlus = Integer.parseInt(args[++i]);
					break;
					
				case "--seed":
					Main.rand = new Random(Long.parseLong(args[++i]));
					break;
				case "-S": case "--rand":
					Main.rand = new Random();
					break;
					
				case "-s": case "--size": case "--spawnSize":
					Main.spawnSize = Integer.parseInt(args[++i]);
					break;
					
				case "--iScale":
					Main.iScale = Double.parseDouble(args[++i]);
					break;
				
				case "--distro":
					Main.distroL = Double.parseDouble(args[++i]);
					Main.distroU = Double.parseDouble(args[++i]);
					Main.distroD = Integer.parseInt(args[++i]);
					break;
				case "--distroL":
					Main.distroL = Double.parseDouble(args[++i]);
					break;
				case "--distroU":
					Main.distroU = Double.parseDouble(args[++i]);
					break;
				case "--distroD":
					Main.distroD = Integer.parseInt(args[++i]);
					break;
					
				case "--threads":
					Main.threads = Integer.parseInt(args[++i]);
					break;
					
				case "--frameSkip":
					Main.frameSkip = Integer.parseInt(args[++i]);
					break;
					
				case "-g": case "--graphics":
					vstr = args[++i];
					v = true;
					if ("+-".indexOf(vstr.charAt(0)) == -1) {
						Main.vectorGraphics = false;
						Main.rasterGraphics = false;
						Main.vectorTrails = false;
					}
					for (int j = 0; j < vstr.length(); j++) {
						char c = vstr.charAt(j);
						switch (Character.toString(c)) {
						case "+": case "-":
							v = c == '+';
							break;
						case "v":
							Main.vectorGraphics = v;
							break;
						case "r":
							Main.rasterGraphics = v;
							break;
						case "t":
							Main.vectorTrails = v;
							break;
						case "_":
							break;
						default:
							errf("unrecognized character in argument `%2$s'"
									+ " for option `%1$s': '%3$s' (@ %4$d)%n",
									arg, vstr, c, j);
							break;
						}
					}
					break;
					
				case "--stats":
					vstr = args[++i];
					v = true;
					if ("+-".indexOf(vstr.charAt(0)) == -1) {
						Main.loadStats = false;
						Main.saveStats = false;
						Main.savePerformanceStats = false;
					}
					for (int j = 0; j < vstr.length(); j++) {
						char c = vstr.charAt(j);
						switch (Character.toString(c)) {
						case "+": case "-":
							v = c == '+';
							break;
						case "l":
							Main.loadStats = v;
							break;
						case "s":
							Main.saveStats = v;
							break;
						case "p":
							Main.savePerformanceStats = v;
							break;
						case "_":
							break;
						default:
							errf("unrecognized character in argument `%2$s'"
									+ " for option `%1$s': '%3$s' (@ %4$d)%n",
									arg, vstr, c, j);
							break;
						}
					}
					break;
					
				case "-o": case "--record":
					Main.saveReplay = true;
					break;
				case "-O": case "--norecord":
					Main.saveReplay = false;
					break;
				case "-p": case "--replay":
					Main.playReplay = true;
					break;
				case "-P": case "--noreplay":
					Main.playReplay = false;
					
				case "-c": case "--useChrisIO":
					Main.useChrisIO = true;
					break;
				case "-C": case "--noChrisIO":
					Main.useChrisIO = false;
					break;
					
				case "--deflate":
					Main.deflateIntensity = Integer.parseInt(args[++i]);
					break;
				case "-z":
					Main.deflateIntensity = 1;
					break;
				case "-Z":
					Main.deflateIntensity = 0;
					break;
					
				default:
					errf("unrecognized option: `%$1s'%n", arg);
					break;
					
				}
				
			} catch (NumberFormatException e) {
				errf("invalid numeric argument for option `%1$s': `%2$s'%n",
						arg, args[i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				errf("missing argument for option `%1$s'%n", arg);
			}
		}
	}
	
	
	
	
	static String repeat(char c, int n) {
		return new String(new char[n]).replace('\0', c);
	}
	
	static final String HR = repeat('-', 80);
	static final String HR_LIGHT = "\33[37m" + HR + "\33[39m";
	
	static final String SPACE4 = repeat(' ', 4);
	static final String SPACE12 = repeat(' ', 12);
	
	private static Pattern ANSI = Pattern.compile("(\33\\[|\233)[\40-\57]*[\60-\77]*[\100-\176]|\33[@-_]");
	private static Pattern FOPT = Pattern.compile("(--?)([-A-Za-z0-9_?]+)");
	
	static class Help {
		String name;
		boolean listed;
		String regex, usage, shortHelp, longHelp;
		Object[] longArgs;
		
		public Help(String n, boolean bl, String re, String u, String sh, String lh, Object[] la) {
			name = n;
			listed = bl;
			regex = re;
			usage = u;
			shortHelp = sh;
			longHelp = lh;
			longArgs = la;
		}
		
		public Help(Help h) {
			this(h.getName(), h.isListed(), h.getRegex(), h.getUsage(),
					h.getShort(), h.getLong(), h.getLongArgs());
		}
		
		public Help(String n, String u, String sh, String lh, Object... la) {
			this(n, true, "$", u, sh, lh, la);
		}
		
		public Help(Help h, String re, String u) {
			this(h.getName()+"("+re+")", false, re+"$", u,
					h.getShort(), h.getLong(), h.getLongArgs());
		}
		
		public String getName() { return name; }
		public boolean isListed() { return listed; }
		public String getRegex() { return regex; }
		public String getUsage() { return usage; }
		public String getShort() { return shortHelp; }
		public String getLong() { return longHelp; }
		public Object[] getLongArgs() { return longArgs; }
		
		public List<Help> genSecondaries() {
			String u = getUsage();
			String f = filterUsage(u);
			Matcher ansimf = ANSI.matcher(f);
			String fnat = ansimf.replaceAll("").trim();
			String[] forms = fnat.split("\\s*\\|\\|\\s*");
			List<List<String>> opts = new ArrayList<>();
			for (String form : forms) {
				List<String> fopts = new ArrayList<>();
				opts.add(fopts);
				String[] parts = form.split("\\s*\\|\\s*");
				for (String part : parts) {
					Matcher partm = FOPT.matcher(part);
					if (!partm.find()) {
						break;
					}
					fopts.add(partm.group());
					if (!partm.hitEnd()) {
						break;
					}
				}
			}
			List<Help> h2a = new ArrayList<Help>();
			String[] nuForms = u.split("\\|\\|");
			String pfx = "";
			String sfx = "||" + u;
			for (int i = 0; i < nuForms.length; i++) {
				String mf = nuForms[i];
				sfx = sfx.substring(mf.length() + 2);
				String nf = mf.replaceAll("__([-A-Za-z0-9_?]+)!_", "{_$1_}");
				List<String> fopts = opts.get(i);
				for (String fopt : fopts) {
					String re = "(?<!-)" + Pattern.quote(fopt);
					String repl = Matcher.quoteReplacement("{@" + fopt + "@}");
					String nu = pfx + nf.replaceAll(re, repl) + sfx;
					h2a.add(new Help(this, re, nu));
				}
				pfx += mf;
			}
			h2a.add(this);
			return h2a;
		}
	}
	
	
	static Help H_HELP = new Help("help",
			"-h | -? |;|| --help [__option!_]",
			"Print help on command options, then exit",
			"{{'    When this option is specified in its shortened form or as the last argument (without {{?__option!_}}), the list of available options is printed, and the program exits.}}%n%n"
			+ "{{'    When the `{{?--help __option!_}}' form is specified, a more in-depth description of {{?__option!_}}, such as the one that you are currently reading, is printed, and the program exits.}}");
	
	static Help H_ERROREXIT = new Help("errorExit",
			"-e | --errorExit",
			"Exit on invalid option" + (errorExit ? " (default)" : ""),
			"{{'    When this option is specified, and an invalid option is encountered, the error is printed and the program exits.}}%n%n"
			+ "{{?{!Related!} }}%n"
			+ "{{$errorWarn}}");
	static Help H_ERRORWARN = new Help("errorWarn",
			"-E | --errorWarn",
			"Ignore invalid options" + (errorExit ? "" : " (default)"),
			"{{'    When this option is specified, and an invalid option is encountered, a warning is printed and the option is ignored.}}%n%n"
			+ "{{?{!Related!} }}%n"
			+ "{{$errorExit}}");
	
	static Help H_DELAY = new Help("delay",
			"-d | --delay | --simSpeed __ms!_",
			"Delay between frames (default={{#%1$d}})",
			"{{'    When this option is specified, the program will delay {{?__ms!_}} milliseconds from the end of one frame to the start of the next.}}%n%n"
			+ "{{?{!Related!} }}%n"
			+ "{{$nodelay}}",
			Main.simSpeed);
	static Help H_NODELAY = new Help("nodelay",
			"-D | --nodelay",
			"Equivalent to `{{?--delay #60!#}}'",
			"%1$s%n%n"
			+ "{{&delay}}",
			HR_LIGHT);
	
	static Help H_TEAMS = new Help("teams",
			"-t | --teams __n!_",
			"Set number of teams (default={{#%1$d}})",
			"{{'   When this option is specified, ships are split into {{?__n!_}} teams instead of the default {{#%1$d}}.}}",
			Main.teams);
	
	static Help H_SHIPS = new Help("ships",
			"-n | --ships | --initialShips __n!_",
			"Set number of ships (default={{#%1$d}})",
			"{{'   When this option is specified, {{?__n!_}} ships are spawned at start instead of the default {{#%1$d}}.}}",
			Main.initialShips);
	
	public static Help[] optHelpAA = {  };
	
	public static Help[] optHelpB = { H_HELP, H_ERROREXIT, H_ERRORWARN, H_DELAY, H_NODELAY, H_TEAMS, H_SHIPS };
	
	public static List<Help> optHelp = new ArrayList<>(Arrays.asList(optHelpAA));
	
	private static void populateOptHelp() {
		for (Help h : optHelpB) {
			optHelp.addAll(h.genSecondaries());
		}
	}
	
	static final String NESC = "(?<!\\\\)";
	private static final String QRLS = Matcher.quoteReplacement(System.lineSeparator());
	
	private static String ra(String s, String regex, String replacement) {
		return s.replaceAll(NESC+"(?:"+regex+")", replacement);
	}
	
	static String filterUsage(String u) {
		return filterUsage(u, "");
	}
	
	static String filterUsage(String u, String indent) {
		u = ra(u, "(?<!\\s)\\s+(!s)", " ");
		u = ra(u, "\\|;", QRLS + indent);
		
		u = ra(u, "[|]", "\33[37m$0\33[39m");
		u = ra(u, "(--?)([-A-Za-z0-9_?]+)", "$1\33[1m$2\33[22m");
		
		u = ra(u, "#([0-7])\\*", "\33[1;3$1m");
		u = ra(u, "\\{\\*", "\33[1;33m");
		u = ra(u, "!#\\*|\\*}", "\33[22;39m");
		u = ra(u, "#([0-7])_", "\33[4;3$1m");
		u = ra(u, "\\{_", "\33[4;33m");
		u = ra(u, "!#_|_}", "\33[24;39m");
		u = ra(u, "#([0-7])@", "\33[7;3$1m");
		u = ra(u, "\\{@", "\33[7;33m");
		u = ra(u, "!#@|@}", "\33[27;39m");
		
		u = ra(u, "#([0-7])", "\33[3$1m");
		u = ra(u, "\\{#", "\33[33m");
		u = ra(u, "!#|#}", "\33[39m");
		
		u = ra(u, "\\{!", "\33[1;4m");
		u = ra(u, "!}", "\33[22;24m");
		
		u = ra(u, "\\*\\*", "\33[1m");
		u = ra(u, "!\\*", "\33[22m");
		u = ra(u, "__", "\33[4m");
		u = ra(u, "!_", "\33[24m");
		u = ra(u, "@@", "\33[7m");
		u = ra(u, "!@", "\33[27m");
		
		u = u.replaceAll("\\\\(.)", "$1");
		return u;
	}
	
	static final Pattern FLP1 = Pattern.compile("\\{\\{([$&])([-A-Za-z0-9_?]+)}}");
	static final Pattern FLP2 = Pattern.compile("\\{\\{([?#])(.*?)}}");
	static final Pattern FLP3 = Pattern.compile("\\{\\{(['])(.*?)}}");
	
	static String late(Pattern p, String lh) {
		Matcher m = p.matcher(lh);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String t = m.group(1);
			String r = null;
			String s = m.group(2);
			Help h = null;
			switch (t) {
			case "$":
				h = findName(s);
				if (h != null)
					r = formatLi(h);
				break;
			case "&":
				h = findName(s);
				if (h != null)
					r = format(h);
				break;
			case "?":
				r = filterUsage(s);
				break;
			case "#":
				r = "\33[36m" + s + "\33[39m";
				break;
			case "'":
				String indent = getIndent(s);
				r = wrapText(s, 70 - indent.length(), indent, indent.length());
				break;
			}
			if (r == null)
				r = m.group();
			r = Matcher.quoteReplacement(r);
			m.appendReplacement(sb, r);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	static String lateFilter(String lh) {
		return late(FLP3, late(FLP2, late(FLP1, lh)));
	}
	
	static final Pattern INDENT = Pattern.compile("\\s*");
	
	public static String getIndent(String s) {
		Matcher m = INDENT.matcher(s);
		m.find();
		return m.group();
	}
	
	private static int wansi(StringBuilder sb, int cols, int i) {
		int w = cols;
		int pei = i;
		int x = 0;
		Matcher m = ANSI.matcher(sb).region(i, sb.length());
		while (x < cols && m.find()) {
			int start = m.start();
			int end = m.end();
			x += start - pei;
			w += end - start;
			pei = end;
		}
		return w;
	}
	
	public static String wrapText(String s, int cols, String indent, int start) {
		String delimiter = System.lineSeparator() + indent;
		StringBuilder sb = new StringBuilder(s);
		int i = start;
		int w = wansi(sb, cols, i);
		while (i + w < sb.length() && (i = sb.lastIndexOf(" ", i + w)) != -1) {
			sb.replace(i, i + 1, delimiter);
			i += delimiter.length();
			w = wansi(sb, cols, i);
		}
		return sb.toString();
	}
	
	
	public static Help findName(String name) {
		for (Help h : optHelpB) {
			if (h.getName().equals(name))
				return h;
		}
		return null;
	}
	
	
	public static void help() {
		System.out.format(filterUsage("Usage: Main [__options...!_]%n%n"
				+ "{!Options!* (`Main --help [option]' for more info)!}%n"));
		for (Help h : optHelp) {
			if (h.isListed()) {
				System.out.println(formatLi(h));
			}
		}
		System.out.println();
		System.exit(0);
	}
	
	public static void help(String arg) {
		boolean found = false;
		for (Help h : optHelp) {
			if (arg.matches(h.regex)) {
				found = true;
				System.out.println(format(h));
				break;
			}
		}
		if (!found) {
			System.out.format("No help found for `%1$s'%n", arg);
		}
		System.exit(0);
	}
	
	public static String format(Help h) {
		return String.format("%1$s%n%n%2$s%n", formatLi(h), formatPart(h, h.getLong()));
	}
	
	static String formatPart(Help h, String part) {
		return lateFilter(String.format(part, h.getLongArgs()));
	}
	
	public static String formatLi(Help h) {
		return String.format("%1$s%n%2$s",
				SPACE4 + filterUsage(h.getUsage(), " "), formatPart(h, "{{'" + SPACE12 + h.getShort() + " }}"));
	}
	
	
	
	
	public static void errf(String format, Object... args) {
		System.out.format(format, args);
		if (errorExit) {
			System.out.format("try `Main --help' for help%n");
			System.exit(1);
		}
	}
	
	
	
	static { populateOptHelp(); }
	
}