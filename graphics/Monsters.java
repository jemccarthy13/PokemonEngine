package graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Monsters {
	public String name;
	public int level;
	public int number;
	public int hp;
	public int attack;
	public int def;
	public int spAttack;
	public int spDef;
	public int spd;
	public int base_hp;
	public int base_attack;
	public int base_def;
	public int base_spAttack;
	public int base_spDef;
	public int base_spd;
	public int cur_HP;
	public int cur_attack;
	public int cur_def;
	public int cur_spAttack;
	public int cur_spDef;
	public int cur_spd;
	public int ev_HP;
	public int ev_attack;
	public int ev_def;
	public int ev_spAttack;
	public int ev_spDef;
	public int ev_spd;
	public double exp;
	public double cur_exp;
	private Image back_sprite;
	private Image front_sprite;
	private Image back_sprite_s;
	private Image front_sprite_s;
	private Image party_icon;
	public String move1 = "";
	public String move2 = "";
	public String move3 = "";
	public String move4 = "";
	public int attack_damage;
	public int statusEffect = 0;
	private boolean[] weak = new boolean[16];
	private boolean[] strong = new boolean[16];
	private boolean shiny = false;

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public int getHP() {
		return this.hp;
	}

	public void setCurrentHP(int i) {
		this.cur_HP -= i;
	}

	public void healPokemon() {
		this.cur_HP = this.hp;
		this.cur_attack = this.attack;
		this.cur_def = this.def;
		this.cur_spAttack = this.spAttack;
		this.cur_spDef = this.spDef;
		this.cur_spd = this.spd;
		this.statusEffect = 0;
		System.out.println("Player's Pokemon have been healed back to full.");
	}

	public int getCurrentHP() {
		return this.cur_HP;
	}

	public Image getFrontSprite() {
		if (this.shiny) {
			return this.front_sprite_s;
		}
		return this.front_sprite;
	}

	public Image getBackSprite() {
		if (this.shiny) {
			return this.back_sprite_s;
		}
		return this.back_sprite;
	}

	public Image getIcon() {
		return this.party_icon;
	}

	public boolean[] getWeak() {
		return this.weak;
	}

	public boolean[] getStrong() {
		return this.strong;
	}

	public double getEXP() {
		return this.cur_exp;
	}

	public void levelUp() {
		this.level += 1;
		this.cur_exp = 0.0D;
		this.exp = Math.pow(this.level, 3.0D);
	}

	public double getLevelEXP() {
		return this.exp;
	}

	public int getNumber() {
		return this.number;
	}

	public void create(int n) {
		if (n == 4) {
			this.name = "Charmander";
			this.level = 5;
			this.number = 4;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/004b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/004.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/004sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/004s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon004.png");
			this.hp = 20;
			this.cur_HP = 20;
			this.exp = 200.0D;
			this.cur_exp = 0.0D;
			this.attack = 14;
			this.cur_attack = 14;
			this.def = 15;
			this.cur_def = 15;
			this.spAttack = 40;
			this.spDef = 10;
			this.move1 = "Ember";
			this.move2 = "Scratch";
			this.move3 = "Tail Whip";
			this.move4 = "Fire Spin";
		} else if (n == 220) {
			this.name = "Swinub";
			this.level = 3;
			this.number = 220;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/220b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/220.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/220sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/220s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon220.png");
			this.hp = 15;
			this.cur_HP = 15;
			this.exp = 200.0D;
			this.cur_exp = 0.0D;
			this.attack = 15;
			this.cur_attack = 15;
			this.def = 20;
			this.cur_def = 20;
			this.spAttack = 40;
			this.spDef = 10;
			this.move1 = "Icy Wind";
			this.move2 = "Scratch";
			this.move3 = "Dig";
			this.move4 = "Growl";
		} else if (n == 158) {
			this.name = "Totodile";
			this.level = 5;
			this.number = 158;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/158b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/158.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/158sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/158s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon158.png");
			this.hp = 25;
			this.cur_HP = 25;
			this.exp = 113.0D;
			this.cur_exp = 0.0D;
			this.attack = 16;
			this.cur_attack = 16;
			this.def = 30;
			this.cur_def = 30;
			this.spAttack = 40;
			this.spDef = 10;
			this.move1 = "Water Gun";
			this.move2 = "Scratch";
			this.move3 = "Tail Whip";
			this.move4 = "Toxic";
		} else if (n == 25) {
			this.name = "Pikachu";
			this.level = 5;
			this.number = 25;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/025b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/025.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/025sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/025s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon025.png");
			this.hp = 23;
			this.cur_HP = 23;
			this.exp = 200.0D;
			this.cur_exp = 0.0D;
			this.attack = 12;
			this.cur_attack = 12;
			this.def = 10;
			this.cur_def = 10;
			this.spAttack = 40;
			this.spDef = 10;
			this.cur_exp = 0.0D;
			this.move1 = "Thundershock";
			this.move2 = "Quick Attack";
			this.move3 = "Tail Whip";
			this.move4 = "Thunderwave";
		} else if (n == 198) {
			this.name = "Murkrow";
			this.level = 4;
			this.number = 198;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/198b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/198.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/198sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/198s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon198.png");
			this.hp = 19;
			this.cur_HP = 19;
			this.exp = 200.0D;
			this.cur_exp = 0.0D;
			this.attack = 13;
			this.cur_attack = 13;
			this.def = 15;
			this.cur_def = 15;
			this.spAttack = 10;
			this.spDef = 10;
			this.move1 = "Peck";
			this.move2 = "Pursuit";
			this.move3 = "Quick Attack";
			this.move4 = "Growl";
		} else {
			this.name = "MissingNo";
			this.level = 255;
			this.number = 0;
			this.back_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/000b.png");
			this.front_sprite = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/000.png");
			this.back_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/000sb.png");
			this.front_sprite_s = Toolkit.getDefaultToolkit().createImage("Graphics/Battlers/000s.png");
			this.party_icon = Toolkit.getDefaultToolkit().createImage("Graphics/Icons/icon000.png");
			this.hp = 255;
			this.attack = 255;
			this.exp = 200.0D;
			this.cur_exp = 0.0D;
			this.cur_attack = 255;
			this.def = 255;
			this.cur_def = 255;
			this.def = 255;
			this.spAttack = 255;
			this.spDef = 255;
			this.move1 = "Sky Attack";
			this.move2 = "Flamethrower";
			this.move3 = "Tackle";
			this.move4 = "Hyper Beam";
		}
	}

	public void loadPokemon() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Data/pokemon.txt"));
			String line = "";
			StringTokenizer tokens = new StringTokenizer(line);
			this.number = Integer.parseInt(reader.readLine());
			System.out.println(this.number);
			this.name = reader.readLine();
			System.out.println(this.name);
			String internalname = reader.readLine();
			System.out.println(internalname);
			String kind = reader.readLine();
			System.out.println(kind);
			String pokedexEntry = reader.readLine();
			System.out.println(pokedexEntry);
			String type1 = reader.readLine();
			System.out.println(type1);
			String type2 = reader.readLine();
			System.out.println(type2);
			line = reader.readLine();
			tokens = new StringTokenizer(line);
			this.base_hp = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_hp);
			this.base_attack = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_attack);
			this.base_spAttack = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_spAttack);
			this.base_def = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_def);
			this.base_spDef = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_spDef);
			this.base_spd = Integer.parseInt(tokens.nextToken());
			System.out.println(this.base_spd);
			int rareness = Integer.parseInt(reader.readLine());
			System.out.println(rareness);
			int baseEXP = Integer.parseInt(reader.readLine());
			System.out.println(baseEXP);
			int happiness = Integer.parseInt(reader.readLine());
			System.out.println(happiness);
			String growthrate = reader.readLine();
			System.out.println(growthrate);
			int stepstohatch = Integer.parseInt(reader.readLine());
			System.out.println(stepstohatch);
			String color = reader.readLine();
			System.out.println(color);
			String habitat = reader.readLine();
			System.out.println(habitat);
			line = reader.readLine();
			tokens = new StringTokenizer(line);
			this.ev_HP = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_HP);
			this.ev_attack = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_attack);
			this.ev_spAttack = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_spAttack);
			this.ev_def = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_def);
			this.ev_spDef = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_spDef);
			this.ev_spd = Integer.parseInt(tokens.nextToken());
			System.out.println(this.ev_spd);
			String abilities = reader.readLine();
			System.out.println(abilities);
			String compatibility = reader.readLine();
			System.out.println(compatibility);
			double height = Double.parseDouble(reader.readLine());
			System.out.println(height);
			double weight = Double.parseDouble(reader.readLine());
			System.out.println(weight);
			String genderRate = reader.readLine();
			System.out.println(genderRate);
			String moves = reader.readLine();
			System.out.println(moves);
			String eggmoves = reader.readLine();
			System.out.println(eggmoves);
			line = reader.readLine();
			tokens = new StringTokenizer(line);
			String evolutionName = tokens.nextToken();
			System.out.println(evolutionName);
			String evolutionType = tokens.nextToken();
			System.out.println(evolutionType);
			int evolutionLevel = Integer.parseInt(tokens.nextToken());
			System.out.println(evolutionLevel);
			int battleOffsetPlayer = Integer.parseInt(reader.readLine());
			System.out.println(battleOffsetPlayer);
			int battleOffsetEnemy = Integer.parseInt(reader.readLine());
			System.out.println(battleOffsetEnemy);
			int battleAltitude = Integer.parseInt(reader.readLine());
			System.out.println(battleAltitude);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: graphics.Monsters
 * 
 * JD-Core Version: 0.7.0.1
 */