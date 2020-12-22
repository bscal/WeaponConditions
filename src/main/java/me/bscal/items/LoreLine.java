package me.bscal.items;

public class LoreLine
{
	public final char prefix;
	public final String keyword;
	public final String var;
	public final int index;
	public final boolean contains;

	public LoreLine(char prefix, String keyword, int index, boolean contains, String var)
	{
		this.prefix = prefix;
		this.keyword = keyword;
		this.var = var;
		this.index = index;
		this.contains = contains;
	}

}
