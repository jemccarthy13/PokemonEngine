package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import controller.GameController;
import graphics.GamePanel;
import model.Coordinate;
import party.Battler;
import party.Party;
import scenes.WorldScene;
import storage.Bag;
import storage.Bag.POCKETS;
import storage.Item;

/**
 * The base class for any movable or interactable character or sprite
 */
public class Actor implements Serializable {

	/**
	 * Serialization variable
	 */
	private static final long serialVersionUID = 6292047432930495977L;

	/**
	 * Trainer data for this Actor
	 */
	public ActorData tData = new ActorData();

	private boolean isWalking = false;
	private boolean isRightFoot = false;
	private boolean moved = false;
	protected int animationStep = 1;
	public int animationOffsetX;
	public int animationOffsetY;

	protected Bag inventory = new Bag();

	/**
	 * A representation of facing the top, left, right, or bottom of the screen
	 */
	public static enum DIR {
		/**
		 * The character is facing 'down' (South)
		 */
		SOUTH,
		/**
		 * The character is facing 'left' (West)
		 */
		WEST,
		/**
		 * The character is facing 'right' (East)
		 */
		EAST,
		/**
		 * The character is facing 'up' (North)
		 */
		NORTH;
	}

	/**
	 * Given a starting coordinate, name, and sprite name, create an Actor
	 * 
	 * @param x
	 *            - starting x location
	 * @param y
	 *            - starting y location
	 * @param n
	 *            - the name
	 * @param sprite_name
	 *            - the name of the sprite (from the SpriteLibrary)
	 */
	public Actor(int x, int y, String n, String sprite_name) {
		this.tData.name = n;
		this.tData.position = new Coordinate(x, y);
		this.tData.money = 2000;
		this.tData.party = new Party();
		this.tData.sprite_name = sprite_name;
		setDirection(DIR.SOUTH);
	}

	/**
	 * A default constructor
	 */
	public Actor() {}

	/**
	 * Construct an Actor from some existing trainer data
	 * 
	 * @param data
	 *            - the data to use to construct the Actor
	 */
	public Actor(ActorData data) {
		this.tData = data;
		setDirection(DIR.SOUTH);
	}

	/**
	 * This Actor caught a wild battler
	 * 
	 * @param p
	 */
	public void caughtWild(Battler p) {
		this.tData.party.add(p);
	}

	/**
	 * Return the list of party members
	 * 
	 * @return a list of party members
	 */
	public Party getParty() {
		return this.tData.party;
	}

	/**
	 * Move the character in the given direction
	 * 
	 * @param dir
	 *            - the direction to move
	 */
	public void moveDir(DIR dir) {
		for (int x = 1; x <= 16; x++) {
			changeSprite(x, this.isRightFoot);
			if (x == 16) {
				this.isRightFoot = !this.isRightFoot;
			}
			this.animationStep = x;
			switch (dir) {
			case SOUTH:
				this.animationOffsetY = this.animationStep * 2;
				break;
			case WEST:
				this.animationOffsetX = this.animationStep * -2;
				break;
			case EAST:
				this.animationOffsetX = this.animationStep * 2;
				break;
			case NORTH:
				this.animationOffsetY = this.animationStep * -2;
				break;
			default:
				break;
			}

			WorldScene.instance.render(GamePanel.getInstance().getGraphics(), GamePanel.getInstance().gameController);
			try {
				Thread.sleep(35);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.animationOffsetX = 0;
		this.animationOffsetY = 0;
		this.tData.position = this.tData.position.move(dir);
	}

	/**
	 * Move the character in the given direction
	 * 
	 * @param dir
	 *            - the direction to move
	 */
	public void move(DIR dir) {
		this.tData.position = this.tData.position.move(dir);
	}

	public boolean hasMoved() {
		return this.moved;
	}

	public void doAnimation(GameController gameController) {
		this.moved = false;
		if (isWalking()) {
			this.animationStep += 1;
			changeSprite(this.animationStep, this.isRightFoot);
		}

		// check for animation completion
		// when walking animation is done, handle poison damage
		if (this.animationStep >= 16) {
			this.animationStep = 0; // reset animation counter
			gameController.getPlayer().setWalking(false);
			this.isRightFoot = !this.isRightFoot;
			move(getDirection());
			this.moved = true;
		}
	}

	/**
	 * Change the sprite based on animation variables
	 * 
	 * @param animationStage
	 *            - the current stage of animation
	 * @param rightFoot
	 *            - whether the Actor is on the right foot or not
	 */
	public void changeSprite(int animationStage, boolean rightFoot) {

		int direction = 3 * getDirection().ordinal();

		if (animationStage > 8 && animationStage < 15) {
			int offset = (rightFoot) ? 2 : 1;
			this.tData.sprite = (this.tData.getSprites().get(direction + offset));
		} else {
			this.tData.sprite = (this.tData.getSprites().get(direction));
		}
	}

	/**
	 * Returns whether or not an Actor can talk to another Actor
	 * 
	 * @param other
	 *            - the other Actor
	 * @return whether or not conversation can happen
	 * 
	 * @TODO - analyze this for borderNPC check / trainer battle
	 */
	public boolean getTalkable(Actor other) {
		if (other.getCurrentY() + 1 == this.tData.position.getY()) {
			if (other.getCurrentX() == this.tData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentY() - 1 == this.tData.position.getY()) {
			if (other.getCurrentX() == this.tData.position.getX()) {
				return true;
			}
		}
		if (other.getCurrentX() + 1 == this.tData.position.getX()) {
			if (other.getCurrentY() == this.tData.position.getY()) {
				return true;
			}
		}
		if (other.getCurrentX() - 1 == this.tData.position.getX()) {
			if (other.getCurrentY() == this.tData.position.getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set the amount of money this Actor has
	 * 
	 * @param m
	 *            - how much money
	 */
	public void setMoney(int m) {
		this.tData.money = m;
	}

	/**
	 * Get the amount of money this Actor has
	 * 
	 * @return how much money
	 */
	public int getMoney() {
		return this.tData.money;
	}

	/**
	 * @return the current list of items
	 */
	public Bag getInventory() {
		return this.inventory;
	}

	/**
	 * Add an item to the inventory
	 * 
	 * @param pocket
	 *            - the pocket to add to
	 * @param it
	 *            - the item to add
	 */
	public void addItemToInventory(POCKETS pocket, Item it) {
		this.inventory.addToPocket(pocket, it);
	}

	/**
	 * Set the direction of the Actor
	 * 
	 * @param direction
	 *            - the direction to set to
	 */
	public void setDirection(DIR direction) {
		this.tData.dir = direction;
		this.tData.sprite = this.tData.getSprites().get(direction.ordinal() * 3);
	}

	/**
	 * Get the current direction of this Actor
	 * 
	 * @return current direction
	 */
	public DIR getDirection() {
		return this.tData.dir;
	}

	/**
	 * Set this Actor to face in the opposite direction of the given direction
	 * 
	 * @param dir
	 *            - the other direction
	 */
	public void setDirectionOpposite(DIR dir) {
		switch (dir) {
		case NORTH:
			setDirection(DIR.SOUTH);
			break;
		case EAST:
			setDirection(DIR.WEST);
			break;
		case WEST:
			setDirection(DIR.EAST);
			break;
		case SOUTH:
			setDirection(DIR.NORTH);
			break;
		default:
			break;
		}
	}

	/**
	 * Set this Actor to the opposite direction of it's current direction
	 */
	public void turnAround() {
		setDirectionOpposite(getDirection());
	}

	/**
	 * Get the current X position of this Actor
	 * 
	 * @return - current x position
	 */
	public int getCurrentX() {
		return this.tData.position.getX();
	}

	/**
	 * Get the current Y position of this Actor
	 * 
	 * @return - current y position
	 */
	public int getCurrentY() {
		return this.tData.position.getY();
	}

	/**
	 * Set the current position of this Actor
	 * 
	 * @param c
	 *            - the coordinate to set position to
	 */
	public void setLoc(Coordinate c) {
		this.tData.position = c;
	}

	/**
	 * Set the current name of this Actor
	 * 
	 * @param nameSelected
	 *            - which name to use
	 */
	public void setName(String nameSelected) {
		this.tData.name = nameSelected;
	}

	/**
	 * Gets the number of party members owned
	 * 
	 * @return a string of the number of PartyMembers owned (used to print trainer
	 *         data)
	 * 
	 * @TODO replace with a number
	 */
	public String getNumPokemonOwned() {
		return String.valueOf(this.tData.party.size());
	}

	/**
	 * Get the current Actor's name
	 * 
	 * @return their name
	 */
	public String getName() {
		return this.tData.name;
	}

	/**
	 * Get the current Actor's conversation text
	 * 
	 * @return a list of conversation strings
	 */
	public ArrayList<String> getConversationText() {
		return this.tData.conversationText;
	}

	/**
	 * Get the current stage of conversation text
	 * 
	 * @TODO compare with other conversation utilities and determine if all are
	 *       necessary
	 * @param stage
	 *            - the stage of the conversation
	 * @return conversation message
	 */
	public String getText(int stage) {
		return this.tData.conversationText.get(stage);
	}

	/**
	 * Get the length of conversation text
	 * 
	 * @TODO - remove
	 * @return length of conversation
	 */
	public int getTextLength() {
		return this.tData.conversationText.size();
	}

	/**
	 * Set the Actor to be stationary
	 * 
	 * @param b
	 *            - the status of stationary or not
	 */
	public void setStationary(boolean b) {
		this.tData.stationary = b;
	}

	/**
	 * Return whether or not this Actor is stationary or not.
	 * 
	 * @return is stationary
	 */
	public boolean isStationary() {
		return this.tData.stationary;
	}

	/**
	 * Get whether or not the Actor is a trainer (has a battling party)
	 * 
	 * @return is it a trainer
	 */
	public boolean isTrainer() {
		return this.tData.trainer;
	}

	/**
	 * Get the Actor's current position
	 * 
	 * @return current position
	 */
	public Coordinate getPosition() {
		return this.tData.position;
	}

	public boolean isWalking() {
		return this.isWalking;
	}

	public void setWalking(boolean isWalking) {
		this.isWalking = isWalking;
	}
}
