// Generated from Prism.g4 by ANTLR 4.5

  package evochecker.parser.src.gen;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrismLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, ASSIGN=33, EVOLVE=34, CONST=35, DISTRIBUTION=36, FUNCTIONIDENTIFIER=37, 
		CONSTANTTYPE=38, SLCOMMENT=39, DTMC=40, CTMC=41, MDP=42, BOOLEAN=43, OPERATOR=44, 
		ID=45, INT=46, DOUBLE=47, STRING=48, WS=49;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "ASSIGN", 
		"EVOLVE", "CONST", "DISTRIBUTION", "FUNCTIONIDENTIFIER", "CONSTANTTYPE", 
		"SLCOMMENT", "DTMC", "CTMC", "MDP", "BOOLEAN", "OPERATOR", "ID", "INT", 
		"DOUBLE", "STRING", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'module'", "'endmodule'", "'['", "']'", "','", "'rewards'", "'endrewards'", 
		"':'", "';'", "'('", "')'", "'formula'", "'bool'", "'init'", "'..'", "'->'", 
		"'!'", "'+'", "'''", "'&'", "'>'", "'>='", "'<'", "'<='", "'!='", "'|'", 
		"'*'", "'/'", "'-'", "'<=>'", "'=>'", "'?'", "'='", "'evolve'", "'const'", 
		"'distribution'", null, null, null, "'dtmc'", "'ctmc'", "'mdp'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "ASSIGN", "EVOLVE", 
		"CONST", "DISTRIBUTION", "FUNCTIONIDENTIFIER", "CONSTANTTYPE", "SLCOMMENT", 
		"DTMC", "CTMC", "MDP", "BOOLEAN", "OPERATOR", "ID", "INT", "DOUBLE", "STRING", 
		"WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PrismLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Prism.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\63\u0175\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26"+
		"\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34"+
		"\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3"+
		"#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3"+
		"%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\5&\u010a\n&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\5\'\u0119\n\'\3(\3(\3(\3(\7(\u011f\n(\f(\16(\u0122\13(\3(\5(\u0125"+
		"\n(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3,"+
		"\3,\3,\3,\3,\3,\5,\u0142\n,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-"+
		"\5-\u0152\n-\3.\3.\7.\u0156\n.\f.\16.\u0159\13.\3/\6/\u015c\n/\r/\16/"+
		"\u015d\3\60\5\60\u0161\n\60\3\60\3\60\3\60\3\61\3\61\7\61\u0168\n\61\f"+
		"\61\16\61\u016b\13\61\3\61\3\61\3\62\6\62\u0170\n\62\r\62\16\62\u0171"+
		"\3\62\3\62\3\u0120\2\63\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63"+
		"\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62"+
		"c\63\3\2\t\6\2,-//\61\61>>\5\2##((~~\5\2C\\aac|\6\2\62;C\\aac|\3\2\62"+
		";\4\2$$^^\5\2\13\f\17\17\"\"\u018b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2"+
		"O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3"+
		"\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\3e\3\2\2\2\5l\3\2\2"+
		"\2\7v\3\2\2\2\tx\3\2\2\2\13z\3\2\2\2\r|\3\2\2\2\17\u0084\3\2\2\2\21\u008f"+
		"\3\2\2\2\23\u0091\3\2\2\2\25\u0093\3\2\2\2\27\u0095\3\2\2\2\31\u0097\3"+
		"\2\2\2\33\u009f\3\2\2\2\35\u00a4\3\2\2\2\37\u00a9\3\2\2\2!\u00ac\3\2\2"+
		"\2#\u00af\3\2\2\2%\u00b1\3\2\2\2\'\u00b3\3\2\2\2)\u00b5\3\2\2\2+\u00b7"+
		"\3\2\2\2-\u00b9\3\2\2\2/\u00bc\3\2\2\2\61\u00be\3\2\2\2\63\u00c1\3\2\2"+
		"\2\65\u00c4\3\2\2\2\67\u00c6\3\2\2\29\u00c8\3\2\2\2;\u00ca\3\2\2\2=\u00cc"+
		"\3\2\2\2?\u00d0\3\2\2\2A\u00d3\3\2\2\2C\u00d5\3\2\2\2E\u00d7\3\2\2\2G"+
		"\u00de\3\2\2\2I\u00e4\3\2\2\2K\u0109\3\2\2\2M\u0118\3\2\2\2O\u011a\3\2"+
		"\2\2Q\u012a\3\2\2\2S\u012f\3\2\2\2U\u0134\3\2\2\2W\u0141\3\2\2\2Y\u0151"+
		"\3\2\2\2[\u0153\3\2\2\2]\u015b\3\2\2\2_\u0160\3\2\2\2a\u0165\3\2\2\2c"+
		"\u016f\3\2\2\2ef\7o\2\2fg\7q\2\2gh\7f\2\2hi\7w\2\2ij\7n\2\2jk\7g\2\2k"+
		"\4\3\2\2\2lm\7g\2\2mn\7p\2\2no\7f\2\2op\7o\2\2pq\7q\2\2qr\7f\2\2rs\7w"+
		"\2\2st\7n\2\2tu\7g\2\2u\6\3\2\2\2vw\7]\2\2w\b\3\2\2\2xy\7_\2\2y\n\3\2"+
		"\2\2z{\7.\2\2{\f\3\2\2\2|}\7t\2\2}~\7g\2\2~\177\7y\2\2\177\u0080\7c\2"+
		"\2\u0080\u0081\7t\2\2\u0081\u0082\7f\2\2\u0082\u0083\7u\2\2\u0083\16\3"+
		"\2\2\2\u0084\u0085\7g\2\2\u0085\u0086\7p\2\2\u0086\u0087\7f\2\2\u0087"+
		"\u0088\7t\2\2\u0088\u0089\7g\2\2\u0089\u008a\7y\2\2\u008a\u008b\7c\2\2"+
		"\u008b\u008c\7t\2\2\u008c\u008d\7f\2\2\u008d\u008e\7u\2\2\u008e\20\3\2"+
		"\2\2\u008f\u0090\7<\2\2\u0090\22\3\2\2\2\u0091\u0092\7=\2\2\u0092\24\3"+
		"\2\2\2\u0093\u0094\7*\2\2\u0094\26\3\2\2\2\u0095\u0096\7+\2\2\u0096\30"+
		"\3\2\2\2\u0097\u0098\7h\2\2\u0098\u0099\7q\2\2\u0099\u009a\7t\2\2\u009a"+
		"\u009b\7o\2\2\u009b\u009c\7w\2\2\u009c\u009d\7n\2\2\u009d\u009e\7c\2\2"+
		"\u009e\32\3\2\2\2\u009f\u00a0\7d\2\2\u00a0\u00a1\7q\2\2\u00a1\u00a2\7"+
		"q\2\2\u00a2\u00a3\7n\2\2\u00a3\34\3\2\2\2\u00a4\u00a5\7k\2\2\u00a5\u00a6"+
		"\7p\2\2\u00a6\u00a7\7k\2\2\u00a7\u00a8\7v\2\2\u00a8\36\3\2\2\2\u00a9\u00aa"+
		"\7\60\2\2\u00aa\u00ab\7\60\2\2\u00ab \3\2\2\2\u00ac\u00ad\7/\2\2\u00ad"+
		"\u00ae\7@\2\2\u00ae\"\3\2\2\2\u00af\u00b0\7#\2\2\u00b0$\3\2\2\2\u00b1"+
		"\u00b2\7-\2\2\u00b2&\3\2\2\2\u00b3\u00b4\7)\2\2\u00b4(\3\2\2\2\u00b5\u00b6"+
		"\7(\2\2\u00b6*\3\2\2\2\u00b7\u00b8\7@\2\2\u00b8,\3\2\2\2\u00b9\u00ba\7"+
		"@\2\2\u00ba\u00bb\7?\2\2\u00bb.\3\2\2\2\u00bc\u00bd\7>\2\2\u00bd\60\3"+
		"\2\2\2\u00be\u00bf\7>\2\2\u00bf\u00c0\7?\2\2\u00c0\62\3\2\2\2\u00c1\u00c2"+
		"\7#\2\2\u00c2\u00c3\7?\2\2\u00c3\64\3\2\2\2\u00c4\u00c5\7~\2\2\u00c5\66"+
		"\3\2\2\2\u00c6\u00c7\7,\2\2\u00c78\3\2\2\2\u00c8\u00c9\7\61\2\2\u00c9"+
		":\3\2\2\2\u00ca\u00cb\7/\2\2\u00cb<\3\2\2\2\u00cc\u00cd\7>\2\2\u00cd\u00ce"+
		"\7?\2\2\u00ce\u00cf\7@\2\2\u00cf>\3\2\2\2\u00d0\u00d1\7?\2\2\u00d1\u00d2"+
		"\7@\2\2\u00d2@\3\2\2\2\u00d3\u00d4\7A\2\2\u00d4B\3\2\2\2\u00d5\u00d6\7"+
		"?\2\2\u00d6D\3\2\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7x\2\2\u00d9\u00da"+
		"\7q\2\2\u00da\u00db\7n\2\2\u00db\u00dc\7x\2\2\u00dc\u00dd\7g\2\2\u00dd"+
		"F\3\2\2\2\u00de\u00df\7e\2\2\u00df\u00e0\7q\2\2\u00e0\u00e1\7p\2\2\u00e1"+
		"\u00e2\7u\2\2\u00e2\u00e3\7v\2\2\u00e3H\3\2\2\2\u00e4\u00e5\7f\2\2\u00e5"+
		"\u00e6\7k\2\2\u00e6\u00e7\7u\2\2\u00e7\u00e8\7v\2\2\u00e8\u00e9\7t\2\2"+
		"\u00e9\u00ea\7k\2\2\u00ea\u00eb\7d\2\2\u00eb\u00ec\7w\2\2\u00ec\u00ed"+
		"\7v\2\2\u00ed\u00ee\7k\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7p\2\2\u00f0"+
		"J\3\2\2\2\u00f1\u00f2\7o\2\2\u00f2\u00f3\7k\2\2\u00f3\u010a\7p\2\2\u00f4"+
		"\u00f5\7o\2\2\u00f5\u00f6\7c\2\2\u00f6\u010a\7z\2\2\u00f7\u00f8\7h\2\2"+
		"\u00f8\u00f9\7n\2\2\u00f9\u00fa\7q\2\2\u00fa\u00fb\7q\2\2\u00fb\u010a"+
		"\7t\2\2\u00fc\u00fd\7e\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7k\2\2\u00ff"+
		"\u010a\7n\2\2\u0100\u0101\7r\2\2\u0101\u0102\7q\2\2\u0102\u010a\7y\2\2"+
		"\u0103\u0104\7o\2\2\u0104\u0105\7q\2\2\u0105\u010a\7f\2\2\u0106\u0107"+
		"\7n\2\2\u0107\u0108\7q\2\2\u0108\u010a\7i\2\2\u0109\u00f1\3\2\2\2\u0109"+
		"\u00f4\3\2\2\2\u0109\u00f7\3\2\2\2\u0109\u00fc\3\2\2\2\u0109\u0100\3\2"+
		"\2\2\u0109\u0103\3\2\2\2\u0109\u0106\3\2\2\2\u010aL\3\2\2\2\u010b\u010c"+
		"\7k\2\2\u010c\u010d\7p\2\2\u010d\u0119\7v\2\2\u010e\u010f\7f\2\2\u010f"+
		"\u0110\7q\2\2\u0110\u0111\7w\2\2\u0111\u0112\7d\2\2\u0112\u0113\7n\2\2"+
		"\u0113\u0119\7g\2\2\u0114\u0115\7d\2\2\u0115\u0116\7q\2\2\u0116\u0117"+
		"\7q\2\2\u0117\u0119\7n\2\2\u0118\u010b\3\2\2\2\u0118\u010e\3\2\2\2\u0118"+
		"\u0114\3\2\2\2\u0119N\3\2\2\2\u011a\u011b\7\61\2\2\u011b\u011c\7\61\2"+
		"\2\u011c\u0120\3\2\2\2\u011d\u011f\13\2\2\2\u011e\u011d\3\2\2\2\u011f"+
		"\u0122\3\2\2\2\u0120\u0121\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0124\3\2"+
		"\2\2\u0122\u0120\3\2\2\2\u0123\u0125\7\17\2\2\u0124\u0123\3\2\2\2\u0124"+
		"\u0125\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0127\7\f\2\2\u0127\u0128\3\2"+
		"\2\2\u0128\u0129\b(\2\2\u0129P\3\2\2\2\u012a\u012b\7f\2\2\u012b\u012c"+
		"\7v\2\2\u012c\u012d\7o\2\2\u012d\u012e\7e\2\2\u012eR\3\2\2\2\u012f\u0130"+
		"\7e\2\2\u0130\u0131\7v\2\2\u0131\u0132\7o\2\2\u0132\u0133\7e\2\2\u0133"+
		"T\3\2\2\2\u0134\u0135\7o\2\2\u0135\u0136\7f\2\2\u0136\u0137\7r\2\2\u0137"+
		"V\3\2\2\2\u0138\u0139\7v\2\2\u0139\u013a\7t\2\2\u013a\u013b\7w\2\2\u013b"+
		"\u0142\7g\2\2\u013c\u013d\7h\2\2\u013d\u013e\7c\2\2\u013e\u013f\7n\2\2"+
		"\u013f\u0140\7u\2\2\u0140\u0142\7g\2\2\u0141\u0138\3\2\2\2\u0141\u013c"+
		"\3\2\2\2\u0142X\3\2\2\2\u0143\u0152\t\2\2\2\u0144\u0145\7>\2\2\u0145\u0152"+
		"\7?\2\2\u0146\u0147\7@\2\2\u0147\u0152\7?\2\2\u0148\u0152\4?@\2\u0149"+
		"\u014a\7#\2\2\u014a\u0152\7?\2\2\u014b\u0152\t\3\2\2\u014c\u014d\7>\2"+
		"\2\u014d\u014e\7?\2\2\u014e\u0152\7@\2\2\u014f\u0150\7?\2\2\u0150\u0152"+
		"\7@\2\2\u0151\u0143\3\2\2\2\u0151\u0144\3\2\2\2\u0151\u0146\3\2\2\2\u0151"+
		"\u0148\3\2\2\2\u0151\u0149\3\2\2\2\u0151\u014b\3\2\2\2\u0151\u014c\3\2"+
		"\2\2\u0151\u014f\3\2\2\2\u0152Z\3\2\2\2\u0153\u0157\t\4\2\2\u0154\u0156"+
		"\t\5\2\2\u0155\u0154\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3\2\2\2\u0157"+
		"\u0158\3\2\2\2\u0158\\\3\2\2\2\u0159\u0157\3\2\2\2\u015a\u015c\t\6\2\2"+
		"\u015b\u015a\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e"+
		"\3\2\2\2\u015e^\3\2\2\2\u015f\u0161\5]/\2\u0160\u015f\3\2\2\2\u0160\u0161"+
		"\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0163\7\60\2\2\u0163\u0164\5]/\2\u0164"+
		"`\3\2\2\2\u0165\u0169\7$\2\2\u0166\u0168\n\7\2\2\u0167\u0166\3\2\2\2\u0168"+
		"\u016b\3\2\2\2\u0169\u0167\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016c\3\2"+
		"\2\2\u016b\u0169\3\2\2\2\u016c\u016d\7$\2\2\u016db\3\2\2\2\u016e\u0170"+
		"\t\b\2\2\u016f\u016e\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u016f\3\2\2\2\u0171"+
		"\u0172\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174\b\62\2\2\u0174d\3\2\2\2"+
		"\16\2\u0109\u0118\u0120\u0124\u0141\u0151\u0157\u015d\u0160\u0169\u0171"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}