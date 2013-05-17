package me.awardsunlocked.digihero;
import java.io.*;

public class Hero implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int atk, def, hp, maxhp, xp;
	private String name;
	public static enum ATK_RESULT {HIT, MISS, KO};

	public Hero(int atk, int def, int maxhp, int xp, String name)
	{
		this.atk = atk;
		this.def = def;
		this.hp = maxhp;
		this.maxhp = maxhp;
		this.xp = xp;
		this.name = name;
	}
	

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public ATK_RESULT Attack(Hero attacker)
	{
		if (attacker.atk > this.def) 
		{
			this.hp -= attacker.atk - this.def;
			if (this.hp <= 0)
			{
				return ATK_RESULT.KO;
			}
			return ATK_RESULT.HIT;
		}
		return ATK_RESULT.MISS;
	}
	
	public int Heal(int amount)
	{
		this.hp += amount;
		if (this.hp > this.maxhp)
		{
			this.hp = this.maxhp;
		}
		return this.hp;
	}
	
	
	public void setAtk(int atk)
	{
		this.atk = atk;
	}

	public int getAtk()
	{
		return atk;
	}

	public void setDef(int def)
	{
		this.def = def;
	}

	public int getDef()
	{
		return def;
	}

	public void setHp(int hp)
	{
		this.hp = hp;
	}

	public int getHp()
	{
		return hp;
	}

	public void setMaxhp(int maxhp)
	{
		this.maxhp = maxhp;
	}

	public int getMaxhp()
	{
		return maxhp;
	}

	public void setXp(int xp)
	{
		this.xp = xp;
	}

	public int getXp()
	{
		return xp;
	}
}
