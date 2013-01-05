package controlP5;

/**
 *
 *  2006-2012 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author 		Andreas Schlegel (http://www.sojamo.de)
 * @modified	10/15/2012
 * @version		2.0.2
 *
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

/**
 * <p>
 * Controller is an abstract class that is extended by any available controller within controlP5. this is the full documentation list for
 * all methods available for a controller. An event triggered by a controller will be forwarded to the main program. If a void
 * controlEvent(ControlEvent theEvent) {} method is available, this method will be called.
 * </p>
 * <p>
 * A Controller can notify the main program in 2 different ways:
 * </p>
 * <ul>
 * <li>(1) add method controlEvent(ControlEvent theEvent) to your sketch. ControlP5 will automatically detect this method and will used it
 * to forward any controlEvent triggered by a controller - you can disable forwarding by using setBroadcast(false)
 * {@link controlP5.Controller#setBroadcast(boolean)}</li>
 * <li>(2) each controller requires a unique name when being create. In case an existing name is used for a newly created Controller, the
 * existing one will be overwritten. each unique name can be used to automatically link a controller to either a method or a field within
 * your program.</li>
 * </ul>
 * 
 * @see controlP5.Bang
 * @see controlP5.Button
 * @see controlP5.Knob
 * @see controlP5.Matrix
 * @see controlP5.MultiList
 * @see controlP5.Numberbox
 * @see controlP5.RadioButton
 * @see controlP5.ListBox
 * @see controlP5.Slider
 * @see controlP5.Textarea
 * @see controlP5.Textfield
 * @see controlP5.Textlabel
 * @see controlP5.Toggle
 * @see controlP5.ControlGroup
 * @see controlP5.ControlBehavior
 * @see controlP5.ControlEvent
 * 
 * @example use/ControlP5basics
 */
public abstract class Controller<T> implements ControllerInterface<T>, CDrawable, ControlP5Constants {

	protected PVector position = new PVector();

	protected PVector positionBuffer = new PVector();

	protected PVector absolutePosition = new PVector();

	protected ControllerInterface<?> _myParent;

	protected final String _myName;

	protected ControlWindow _myControlWindow;

	protected boolean isMousePressed = false;

	protected ControlP5 cp5;

	protected int width;

	protected int height;

	protected int _myId = -1;

	protected float _myValue = Float.NaN;

	protected float _myDefaultValue = Float.NaN;

	protected String _myStringValue = "";

	protected float[] _myArrayValue;

	protected Label _myCaptionLabel;

	protected Label _myValueLabel;

	protected boolean isLabelVisible = true;

	protected boolean isMoveable = true;

	protected boolean isBroadcast = true;

	protected boolean isVisible = true;

	protected boolean isActive = false;

	protected boolean isLock = false;

	protected boolean isUserInteraction = true;

	protected boolean isInit = false;

	protected List<ControlListener> _myControlListener;

	protected CColor color = new CColor();

	protected float _myMin;

	protected float _myMax;

	protected float _myUnit;

	protected String target;

	protected Object targetObject;

	protected ControlBehavior _myBehavior;

	protected boolean isBehavior;

	protected List<Controller<?>> subelements;

	protected int _myBroadcastType = FLOAT;

	protected boolean isUpdate = false;

	protected int _myDecimalPoints = 2;

	public static int autoWidth = 49;

	public static int autoHeight = 19;

	public static PVector autoSpacing = new PVector(10, 10, 0);

	protected boolean[] availableImages = new boolean[4];

	protected PImage[] images = new PImage[4];

	protected ControllerView<T> _myControllerView;

	protected ControllerView<T> _myDebugView;

	protected int _myDisplayMode = DEFAULT;

	protected int _myPickingColor = 0xffffff00;

	protected boolean mouseover;

	protected String _myAddress = "";

	protected List<ControllerPlug> _myControllerPlugList;

	protected boolean tooltipEnabled;

	protected boolean listening;

	protected boolean isInside = false;

	private T me;

	protected boolean dragged;

	/**
	 * TODO add distribution options for MOVE, RELEASE, and PRESSED. setDecimalPoints: setDcimalPoints(6) does only show 2 digits after the
	 * point
	 */

	/**
	 * Convenience constructor to extend Controller.
	 * 
	 * @example use/ControlP5extendController
	 * @param theControlP5
	 * @param theName
	 */
	public Controller(ControlP5 theControlP5, String theName) {
		this(theControlP5, theControlP5.getDefaultTab(), theName, 0, 0, autoWidth, autoHeight);
		theControlP5.register(theControlP5.papplet, theName, this);
	}

	protected Controller(final ControlP5 theControlP5, final ControllerGroup<?> theParent, final String theName, final float theX, final float theY, final int theWidth, final int theHeight) {
		cp5 = theControlP5;
		me = (T) this;
		if (cp5 == null) {
			isBroadcast = false;
		}
		_myName = theName;

		position = new PVector(theX, theY, 0);
		positionBuffer = new PVector(theX, theY, 0);

		setParent(theParent);
		if (theParent != null) {
			color.set(theParent.color);
		} else {
			color.set(cp5.color);
		}
		width = theWidth;
		height = theHeight;

		_myCaptionLabel = new Label(cp5, theName);
		_myCaptionLabel.setColor(color.getCaptionLabel());
		_myValueLabel = new Label(cp5, "-");
		_myValueLabel.setColor(color.getCaptionLabel());

		_myControllerPlugList = new ArrayList<ControllerPlug>();
		_myControlListener = new ArrayList<ControlListener>();
		subelements = new ArrayList<Controller<?>>();
		_myArrayValue = new float[0];
		_myDebugView = new DebugView();
		setView(_myDebugView);
	}

	List<Controller<?>> getSubelements() {
		return subelements;
	}

	/**
	 * {@inheritDoc}
	 */
	@ControlP5.Layout public String getAddress() {
		return _myAddress;
	}

	@Override @ControlP5.Invisible @ControlP5.Layout public T setAddress(String theAddress) {
		if (_myAddress.length() == 0) {
			_myAddress = theAddress;
		}
		return me;
	}

	protected Controller(final ControlP5 theControlP5, final String theName, final float theX, final float theY, final int theWidth, final int theHeight) {
		this(theControlP5, theControlP5.getTab("default"), theName, theX, theY, theWidth, theHeight);
	}

	protected Controller(final String theName, final int theX, final int theY) {
		_myName = theName;
		position = new PVector(theX, theY, 0);
	}

	@ControlP5.Invisible public void init() {
		_myDefaultValue = _myValue;

		// plug to a method or field inside the main papplet.
		// forwarding a ControlEvent to the controlEvent() method inside
		// the main papplet is done by the controlbroadcaster.
		cp5.getControlBroadcaster().plug(cp5.papplet, this, _myName);
		initControllerValue();
		isInit = cp5.isAutoInitialization;
		setValue(_myDefaultValue);
		isInit = true;
		updateDisplayMode(DEFAULT);
	}

	protected final void initControllerValue() {
		if (_myValue == getMin() || Float.isNaN(_myValue)) {
			if (_myControllerPlugList.size() == 1) {
				if (getControllerPlugList().get(0).getValue() == null) {
					setDefaultValue(getMin());
				} else {
					float myInitValue = 0;
					if (getControllerPlugList().get(0).getValue() instanceof Boolean) {
						final boolean myBoolean = new Boolean(getControllerPlugList().get(0).getValue().toString()).booleanValue();
						myInitValue = (myBoolean == true) ? 1f : 0f;

					} else if (getControllerPlugList().get(0).getValue() instanceof Float) {
						myInitValue = (new Float(getControllerPlugList().get(0).getValue().toString())).floatValue();

					} else if (getControllerPlugList().get(0).getValue() instanceof Integer) {
						myInitValue = (new Integer(getControllerPlugList().get(0).getValue().toString())).intValue();

					} else if (getControllerPlugList().get(0).getValue() instanceof String) {
						_myStringValue = getControllerPlugList().get(0).getValue().toString();
					}
					setDefaultValue(myInitValue);
				}
			} else {
				if (Float.isNaN(getDefaultValue())) {
					setDefaultValue(getMin());
				}
			}
		}
		_myValue = _myDefaultValue;
	}

	protected void updateFont(ControlFont theControlFont) {
		_myCaptionLabel.updateFont(theControlFont);
		_myValueLabel.updateFont(theControlFont);
	}

	/**
	 * with setBehavior you can add a ControlBehavior to a controller. A ControlBehavior can be used to e.g. automatically change state,
	 * function, position, etc.
	 * 
	 * @example ControlP5behavior
	 * @param theBehavior ControlBehavior
	 * @return Controller
	 */
	public T setBehavior(final ControlBehavior theBehavior) {
		isBehavior = true;
		_myBehavior = theBehavior;
		_myBehavior.init(this);
		return me;
	}

	/**
	 * @return Controller
	 */
	public T removeBehavior() {
		isBehavior = false;
		_myBehavior = null;
		return me;
	}

	/**
	 * @return ControlBehavior
	 */
	public ControlBehavior getBehavior() {
		return _myBehavior;
	}

	/**
	 * @return float
	 */
	@ControlP5.Layout public float getDefaultValue() {
		return _myDefaultValue;
	}

	/**
	 * set the default value.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout public T setDefaultValue(final float theValue) {
		_myDefaultValue = theValue;
		return me;
	}

	/**
	 * enable or prevent the controller to be moveable. By default a controller is moveable.
	 * 
	 * @param theValue boolean
	 * @return Controller
	 */
	@ControlP5.Layout public T setMoveable(final boolean theValue) {
		isMoveable = theValue;
		return me;
	}

	/**
	 * checks if a controller is moveable.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout public boolean isMoveable() {
		return isMoveable;
	}

	/**
	 * show or hide the labels of a controller.
	 * 
	 * @param theValue boolean
	 * @return Controller
	 */
	@ControlP5.Layout public T setLabelVisible(final boolean theValue) {
		isLabelVisible = theValue;
		return me;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout public boolean isLabelVisible() {
		return isLabelVisible;
	}

	/**
	 * Use setBroadcast to enable and disable the broadcasting of changes in a controller's value. By default any value changes are
	 * forwarded to function controlEvent inside your program. use setBroadcast(false) to disable forwarding.
	 * 
	 * @param theFlag boolean
	 * @return Controller
	 */
	@ControlP5.Layout public T setBroadcast(final boolean theFlag) {
		isBroadcast = theFlag;
		return me;
	}

	/**
	 * check if broadcasting is enabled or disabled for a controller. Every event relevant for a value change will be broadcasted to any of
	 * the value-listeners. By default broadcasting for a controller is enabled.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout public boolean isBroadcast() {
		return isBroadcast;
	}

	/**
	 * get the position of a controller. e.g. Controller.getPosition().x; {@inheritDoc}
	 */
	@ControlP5.Layout public PVector getPosition() {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Layout public T setPosition(PVector thePVector) {
		position.x = thePVector.x;
		position.y = thePVector.y;
		position.z = thePVector.z;
		positionBuffer.x = thePVector.x;
		positionBuffer.y = thePVector.y;
		positionBuffer.z = thePVector.z;
		return me;
	}

	/**
	 * set the position of a controller. The position of a controller is relative.
	 * 
	 * @param theX float
	 * @param theY float
	 * @return Controller
	 */
	@Override public T setPosition(final float theX, final float theY) {
		position.x = theX;
		position.y = theY;
		positionBuffer.x = position.x;
		positionBuffer.y = position.y;
		return me;
	}

	/**
	 * @return {@link PVector}
	 */
	public PVector getAbsolutePosition() {
		// should return a mutable object of absolutePostion in a new PVector
		// object to prevent absolutePosition from being modified by changing
		// its field values. PVector should have getter and setters for x,y,z
		return absolutePosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Invisible public T setAbsolutePosition(PVector thePVector) {
		// TODO
		// doesnt work properly yet.
		// absolute position should not be changed from the outside anyway.
		absolutePosition.x = thePVector.x;
		absolutePosition.y = thePVector.y;
		absolutePosition.z = thePVector.z;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@ControlP5.Invisible public T updateAbsolutePosition() {
		return me;
	}

	/**
	 * continuousUpdateEvents is used for internal updates of a controller. this method is final and can't be overridden.
	 * 
	 * @exclude
	 */
	@ControlP5.Invisible public final void continuousUpdateEvents() {
		if (isBehavior) {
			if (_myBehavior.isActive() && !isMousePressed) {
				_myBehavior.update();
			}
		}
	}

	/**
	 * updateEvents is used for internal updates of a controller. this method is final and can't be overwritten.
	 */
	@ControlP5.Invisible public final T updateEvents() {
		if (isInside) {
			boolean moved = ((_myControlWindow.mouseX - _myControlWindow.pmouseX) != 0 || (_myControlWindow.mouseY - _myControlWindow.pmouseY) != 0);
			if (isMousePressed) {
				if (moved) {
					onDrag();
					dragged = true;
				}
			} else {
				if (moved && this.equals(_myControlWindow.getFirstFromMouseOverList())) {
					onMove();
				}
			}
		}

		if (isVisible && (isMousePressed == _myControlWindow.mouselock)) {
			if (isMousePressed && cp5.isAltDown() && isMoveable) {
				if (!cp5.isMoveable) {
					positionBuffer.x += _myControlWindow.mouseX - _myControlWindow.pmouseX;
					positionBuffer.y += _myControlWindow.mouseY - _myControlWindow.pmouseY;
					if (cp5.isShiftDown()) {
						position.x = ((int) (positionBuffer.x) / 10) * 10;
						position.y = ((int) (positionBuffer.y) / 10) * 10;
					} else {
						position.set(positionBuffer);
					}
				}
			} else {
				if (!isLock) {
					if (isInside) {
						setMouseOver(true);
					}
					if (inside()) {
						if (!isInside) {
							onEnter();
							setIsInside(true);
						}
						setIsInside(true);
					} else {
						if (isInside && !isMousePressed) {
							onLeave();
							setMouseOver(false);
							setIsInside(false);
						}
						if (!isInside && mouseover) {

							setMouseOver(false);

							// here the mouseOver is set to false when the mouse is released
							// outside a controller. the mouseoverlist is not updated when
							// the mouse is still pressed but has left the controller - updating the
							// list here currently conflicts with callbacks called from inside
							// setMouseOver()
							//
							// Note: the mouseoverlist is only updated for ControllerGroups when
							// the mouse is pressed but is hovering other controllers while mouse is
							// dragged.
						}
					}
				}
			}
		}
		return me;
	}

	public final Pointer getPointer() {
		return new Pointer() {

			public int x() {
				return (int) (_myControlWindow.mouseX - _myParent.getAbsolutePosition().x - position.x);
			}

			public int y() {
				return (int) (_myControlWindow.mouseY - _myParent.getAbsolutePosition().y - position.y);
			}

			public int px() {
				return (int) (_myControlWindow.pmouseX - _myParent.getAbsolutePosition().x - position.x);
			}

			public int py() {
				return (int) (_myControlWindow.pmouseY - _myParent.getAbsolutePosition().y - position.y);
			}
		};
	}

	/**
	 * @param theStatus boolean
	 * @return boolean
	 */
	@ControlP5.Invisible public final boolean setMousePressed(final boolean theStatus) {
		if (!isVisible && !isUserInteraction) {
			return false;
		}
		if (theStatus == true) {
			if (isInside) {
				isMousePressed = true;
				if (!cp5.isAltDown()) {
					mousePressed();
					onPress();
					cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_PRESSED));
				}
				return true;
			}
		} else {
			if (isMousePressed == true && inside()) {
				isMousePressed = false;
				if (!cp5.isAltDown()) {
					if (!dragged) {
						onClick();
					}
					mouseReleased();
					onRelease();
					dragged = false;
					cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_RELEASED));
				}
			}
			if (!inside()) {
				setIsInside(false);
				if (isMousePressed) {
					isMousePressed = false;
					mouseReleasedOutside();
					onReleaseOutside();
					cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_RELEASEDOUTSIDE));
				}
				if (this instanceof Textfield) {
					mouseReleasedOutside();
					onReleaseOutside();
				}
			}
		}
		return false;
	}

	/**
	 * enables a controller to listen to changes made to the variable linked to the controller. Use true to enable and false to disable a
	 * controller from listening to changes.
	 * 
	 * @param theFlag
	 * @return Controller
	 */
	public T listen(boolean theValue) {
		if (theValue == listening) {
			return me;
		}
		listening = theValue;
		if (listening) {
			cp5.listenTo(_myName, cp5.getObjectForController(this));
		} else {
			cp5.stopListeningTo(_myName, cp5.getObjectForController(this));
		}
		return me;
	}

	/**
	 * returns true or false for the current listening status. by default it is set to false
	 * 
	 * @see controlP5.Controller#listen(boolean)
	 * 
	 * @return boolean
	 */
	public boolean isListening() {
		return listening;
	}

	/**
	 * check if the mouse is within this particular controller.
	 * 
	 * @return boolean
	 */
	public boolean isMouseOver() {
		return mouseover;
	}

	public T setMouseOver(boolean theFlag) {
		if (mouseover == theFlag) {
			return me;
		}
		mouseover = theFlag;
		if (mouseover) {
			_myControlWindow.setMouseOverController(this);
			cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_ENTER));
			cp5.getTooltip().activate(this);
		} else {
			cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_LEAVE));
			_myControlWindow.removeMouseOverFor(this);
			cp5.getTooltip().deactivate();
		}
		return me;
	}

	/**
	 * @see ControllerInterface.updateInternalEvents
	 */
	@ControlP5.Invisible public T updateInternalEvents(final PApplet theApplet) {
		return me;
	}

	/**
	 * the default draw function for each controller extending superclass Controller. This draw function will take care of default matrix
	 * operations and will call the display function of the current ControllerView object active for this particular controller.
	 * 
	 * @exclude
	 * @see controlP5.ControllerView
	 * @param theApplet PApplet
	 */
	@ControlP5.Invisible public void draw(final PApplet theApplet) {

		theApplet.pushMatrix();

		theApplet.translate(position.x, position.y);

		_myControllerView.display(theApplet, me);

		theApplet.popMatrix();

		// theApplet.pushMatrix();
		// _myDebugDisplay.display(theApplet, this);
		// theApplet.popMatrix();
	}

	/**
	 * @param theElement ControllerInterface
	 * @return Controller
	 */
	@ControlP5.Invisible public T add(final ControllerInterface<?> theElement) {
		ControlP5.logger().warning(" add() not supported.");
		return me;
	}

	/**
	 * @param theElement ControllerInterface
	 * @return Controller
	 */
	@ControlP5.Invisible public T remove(final ControllerInterface<?> theElement) {
		ControlP5.logger().warning("remove() not supported.");
		return me;
	}

	/**
	 * removes a controller from controlP5.
	 */
	public void remove() {
		_myControlWindow.removeMouseOverFor(this);
		if (_myParent != null) {
			_myParent.remove(this);
		}
		if (cp5 != null) {
			cp5.remove(this);
		}
	}

	@Override public T bringToFront() {
		return bringToFront(this);
	}

	@Override public T bringToFront(ControllerInterface<?> theController) {
		if (_myParent instanceof Tab) {
			moveTo((Tab) _myParent);
		} else {
			_myParent.bringToFront(theController);
		}
		if (theController != this) {
			if (getSubelements().contains(theController)) {
				if (theController instanceof Controller<?>) {
					getSubelements().remove(theController);
					getSubelements().add((Controller<?>) theController);
				}
			}
		}
		return me;
	}

	/**
	 * returns the index name of the controller.
	 * 
	 * @return String
	 */
	public String getName() {
		return _myName;
	}

	/**
	 * moves the controller to another tab. The tab is defined by parameter theTabName. if controlP5 can't find a tab with given name,
	 * controlP5 will create this tab and add it to the main window.
	 * 
	 * @param theTabName String
	 * @return Controller
	 */
	public final T moveTo(final String theTabName) {
		setTab(theTabName);
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theTabName);
		}
		return me;
	}

	/**
	 * moves the controller to another tab.
	 * 
	 * @param theTab
	 * @return Controller
	 */
	public final T moveTo(final Tab theTab) {
		setTab(theTab.getWindow(), theTab.getName());
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theTab);
		}
		return me;
	}

	/**
	 * moves the controller to the default tab inside the main window.
	 * 
	 * @param theApplet
	 * @return Controller
	 */
	public final T moveTo(final PApplet theApplet) {
		setTab("default");
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theApplet);
		}
		return me;
	}

	/**
	 * moves the controller to a tab inside the main window.
	 * 
	 * @param theApplet
	 * @param theTabName
	 */
	public final T moveTo(final PApplet theApplet, final String theTabName) {
		setTab(theTabName);
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theApplet, theTabName);
		}
		return me;
	}

	/**
	 * moves the controller to the default tab of a control window - other than the main window.
	 * 
	 * @param theControlWindow
	 */
	public final T moveTo(final ControlWindow theControlWindow) {
		setTab(theControlWindow, "default");
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theControlWindow);
		}
		return me;
	}

	/**
	 * 
	 * @param theControlWindow
	 * @param theTabName
	 * @return Controller
	 */
	public final T moveTo(final ControlWindow theControlWindow, final String theTabName) {
		setTab(theControlWindow, theTabName);
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theControlWindow, theTabName);
		}
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	public final T moveTo(final ControllerGroup<?> theGroup, final Tab theTab, ControlWindow theControlWindow) {
		for (Controller<?> c : getSubelements()) {
			c.moveTo(theGroup, theTab, theControlWindow);
		}

		if (theGroup != null) {
			setGroup(theGroup);
			return me;
		}

		if (theControlWindow == null) {
			theControlWindow = cp5.controlWindow;
		}

		setTab(theControlWindow, theTab.getName());
		return me;
	}

	/**
	 * 
	 * @param theGroup
	 * @return Controller
	 */
	public final T moveTo(final ControlGroup<?> theGroup) {
		if (theGroup != null) {
			setGroup(theGroup);
		}
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	public final T moveTo(final ControllerGroup<?> theGroup) {
		if (theGroup != null) {
			setGroup(theGroup);
		}
		return me;
	}

	/**
	 * sets the tab of the controller.
	 * 
	 * @param theName String
	 * @return Controller
	 */
	public final T setTab(final String theName) {
		setParent(cp5.getTab(theName));
		for (Controller<?> c : getSubelements()) {
			c.setTab(theName);
		}
		return me;
	}

	/**
	 * 
	 */
	public final T setTab(final ControlWindow theWindow, final String theName) {
		setParent(cp5.getTab(theWindow, theName));
		for (Controller<?> c : getSubelements()) {
			c.setTab(theWindow, theName);
		}
		return me;
	}

	/**
	 * sets the group of the controller.
	 * 
	 * @param theName String
	 * @return Controller
	 */
	public final T setGroup(final String theName) {
		setParent(cp5.getGroup(theName));
		for (Controller<?> c : getSubelements()) {
			c.setGroup(theName);
		}
		return me;
	}

	public final T setGroup(final ControllerGroup<?> theGroup) {
		setParent(theGroup);
		for (Controller<?> c : getSubelements()) {
			c.setGroup(theGroup);
		}
		return me;
	}

	/**
	 * get the instance of the tab the controller belongs to.
	 * 
	 * @return Tab
	 */
	public Tab getTab() {
		if (_myParent instanceof Tab) {
			return (Tab) _myParent;
		}
		return _myParent.getTab();
	}

	/**
	 * set the parent of a parent of a controller. this method is only meant for internal use. this method is final and can't be
	 * overwritten.
	 * 
	 * @param theParent ControllerInterface
	 * @return Controller
	 */
	@ControlP5.Invisible public final T setParent(final ControllerInterface<?> theParent) {
		if (_myParent != null) {
			_myParent.remove(this);
		}
		absolutePosition = new PVector(position.x, position.y);
		if (theParent != null) {
			_myParent = theParent;
			_myParent.add(this);
			absolutePosition.add(_myParent.getPosition());
			_myControlWindow = _myParent.getWindow();
		}
		setMouseOver(false);
		return me;
	}

	/**
	 * returns the parent of a controller.
	 * 
	 * @return ControllerInterface
	 */

	@ControlP5.Invisible @Override @ControlP5.Layout public ControllerInterface<?> getParent() {
		return _myParent;
	}

	/**
	 * returns the control window of the controller
	 * 
	 * @return ControlWindow
	 */
	public ControlWindow getWindow() {
		return _myControlWindow;
	}

	/**
	 * checks if the mouse is within the area of a controller.
	 * 
	 * @return boolean
	 */
	protected boolean inside() {
		return (_myControlWindow.mouseX > position.x + _myParent.getAbsolutePosition().x && _myControlWindow.mouseX < position.x + _myParent.getAbsolutePosition().x + width
				&& _myControlWindow.mouseY > position.y + _myParent.getAbsolutePosition().y && _myControlWindow.mouseY < position.y + _myParent.getAbsolutePosition().y + height);
	}

	/**
	 * returns true or false and indicates if the mouse is inside the area of a controller.
	 * 
	 * @return boolean
	 */
	public boolean isInside() {
		return isInside;
	}

	/**
	 * checks if a controller is active.
	 * 
	 * @return boolean
	 */
	@ControlP5.Layout public boolean isActive() {
		return isActive;
	}

	/**
	 * returns true or false if the mouse has is pressed.
	 * 
	 * @return boolean
	 */
	public boolean isMousePressed() {
		return isMousePressed;
	}

	protected void onEnter() {
	}

	protected void onLeave() {
	}

	protected void onDrag() {
	}

	protected void onMove() {
	}

	protected void onClick() {
	}

	protected void onPress() {
	}

	protected void onRelease() {
	}

	protected void onScroll(int theAmount) {
	}

	protected void onReleaseOutside() {
	}

	protected void mousePressed() {
	}

	protected void mouseReleased() {
	}

	protected void mouseReleasedOutside() {
	}

	protected void setIsInside(boolean theFlag) {
		isInside = theFlag;
	}

	protected boolean getIsInside() {
		return isInside;
	}

	/**
	 * 
	 * @param KeyEvent theEvent
	 */
	@ControlP5.Invisible public void keyEvent(final KeyEvent theEvent) {
	}

	/**
	 * set the id of a controller.
	 * 
	 * @param int theId
	 * @return Controller
	 */
	@ControlP5.Layout public T setId(final int theId) {
		_myId = theId;
		return me;
	}

	/**
	 * returns the id of a controller, by default the id is -1. Any int can be given to a controller as its ID, controlP5 does not recognize
	 * duplicates, this has to be managed on the user site.
	 * 
	 * @return int
	 */
	@ControlP5.Layout public int getId() {
		return _myId;
	}

	protected ControllerPlug getControllerPlug(int theIndex) {
		return _myControllerPlugList.get(theIndex);
	}

	/**
	 * 
	 * @return List<ControllerPlug>
	 */
	public List<ControllerPlug> getControllerPlugList() {
		return _myControllerPlugList;
	}

	protected boolean checkControllerPlug(ControllerPlug thePlug) {
		for (ControllerPlug cp : _myControllerPlugList) {
			if (cp.getObject().equals(thePlug.getObject()) && cp.getName().equals(thePlug.getName())) {
				return true;
			}
		}
		return false;
	}

	protected void addControllerPlug(final ControllerPlug thePlug) {
		if (checkControllerPlug(thePlug))
			return;
		_myControllerPlugList.add(thePlug);
	}

	protected void removeControllerPlug(final ControllerPlug thePlug) {
		_myControllerPlugList.remove(thePlug);
	}

	/**
	 * plugs the controller to a list of objects
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public T plugTo(final Object[] theObjects) {
		for (Object o : theObjects) {
			plugTo(o, _myName);
		}
		return me;
	}

	/**
	 * 
	 * @param theObjects
	 * @param theName
	 * @return Controller
	 */
	public T plugTo(final Object[] theObjects, String theName) {
		for (Object o : theObjects) {
			plugTo(o, theName);
		}
		return me;
	}

	/**
	 * @param theObject
	 * @return Controller
	 */
	public T plugTo(Object theObject) {
		return plugTo(theObject, _myName);
	}

	public T plugTo(Object theObject, String theName) {
		if ((theObject instanceof PApplet)) {
			unplugFrom(theObject);
		}
		cp5.getControlBroadcaster().plug(theObject, this, theName);
		cp5.getControlBroadcaster().plug(theObject, this, cp5.getControlBroadcaster().getEventMethod());
		return me;
	}

	/**
	 * unplugs the controller from a list of objects
	 * 
	 * @param theObjects
	 * @return
	 */
	public T unplugFrom(final Object[] theObjects) {
		for (Object o : theObjects) {
			unplugFrom(o);
		}
		return me;
	}

	/**
	 * unplugs the Controller for a single object
	 * 
	 * @param theObject
	 * @return Controller
	 */
	public T unplugFrom(final Object theObject) {
		for (Iterator<ControllerPlug> i = _myControllerPlugList.iterator(); i.hasNext();) {
			if (i.next().getObject().equals(theObject)) {
				i.remove();
			}
		}
		return me;
	}

	/**
	 * @param theValue float
	 */
	@ControlP5.Layout public T setValue(float theValue) {
		_myValue = theValue;
		broadcast(FLOAT);
		return me;
	}

	/**
	 * @see Controller#getStringValue()
	 * @see Controller#getArrayValue()
	 * @return float
	 */
	@ControlP5.Layout public float getValue() {
		return _myValue;
	}

	/**
	 * @param theValue
	 * @return Controller
	 */
	@ControlP5.Layout public T setStringValue(String theValue) {
		_myStringValue = theValue;
		return me;
	}

	/**
	 * @see Controller#getValue()
	 * @see Controller#getArrayValue()
	 * 
	 * @return String
	 */
	@ControlP5.Layout public String getStringValue() {
		return _myStringValue;
	}

	/**
	 * returns the current float array value of a controller.
	 * 
	 * @see Controller#getValue()
	 * @see Controller#getStringValue()
	 * 
	 * @return float[]
	 */
	@ControlP5.Layout public float[] getArrayValue() {
		return _myArrayValue;
	}

	/**
	 * @param theIndex
	 * @return float
	 */
	public float getArrayValue(int theIndex) {
		if (theIndex >= 0 && theIndex < _myArrayValue.length) {
			return _myArrayValue[theIndex];
		} else {
			return Float.NaN;
		}
	}

	/**
	 * 
	 * @param theArray
	 * @return Controller
	 */
	@ControlP5.Layout public T setArrayValue(float[] theArray) {
		_myArrayValue = theArray;
		return me;
	}

	/**
	 * 
	 * @param theIndex
	 * @param theValue
	 * @return Controller
	 */
	public T setArrayValue(int theIndex, float theValue) {
		if (theIndex >= 0 && theIndex < _myArrayValue.length) {
			_myArrayValue[theIndex] = theValue;
		}
		return me;
	}

	/**
	 * sets the value of the controller without sending the broadcast event. this function is final.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	public final T changeValue(float theValue) {
		boolean br = this.isBroadcast;
		this.isBroadcast = false;
		setValue(theValue);
		this.isBroadcast = br;
		return me;
	}

	/**
	 * updates the value of the controller without having to set the value explicitly. update does not visually update the controller. the
	 * updating status can be set with setUpdate(true/false) and checked with isUpdate().
	 * 
	 * @see Controller#setUpdate(boolean)
	 * @see Controller#isUpdate()
	 * @return Controller
	 */
	public T update() {
		return me;
	}

	/**
	 * disables the update function for a controller.
	 * 
	 * @see Controller#update()
	 * @see Controller#isUpdate()
	 * 
	 * @param theFlag boolean
	 * @return Controller
	 */
	@Override public T setUpdate(final boolean theFlag) {
		isUpdate = theFlag;
		return me;
	}

	/**
	 * enables the update function for a controller.
	 * 
	 * @see Controller#update()
	 * @see Controller#setUpdate(boolean)
	 * 
	 * @return boolean
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	@ControlP5.Invisible public int getPickingColor() {
		return _myPickingColor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Layout public CColor getColor() {
		return color;
	}

	/**
	 * sets the content of the caption label of a controller.
	 * 
	 * @param theLabel
	 * @return Controller
	 */
	@ControlP5.Layout public T setCaptionLabel(final String theLabel) {
		_myCaptionLabel.set(theLabel);
		return me;
	}

	/**
	 * set or change the value of the value label of a controller. (this is cheating, but maybe useful for some cases.)
	 * 
	 * @param theLabel
	 * @return Controller
	 */
	@ControlP5.Layout public T setValueLabel(final String theLabel) {
		_myValueLabel.set(theLabel);
		return me;
	}

	/**
	 * returns the controller's caption label text.
	 * 
	 * @return String
	 */
	@ControlP5.Invisible public String getLabel() {
		return _myCaptionLabel.getText();
	}

	/**
	 * @see controlP5.ControlListener
	 * @param theListener ControlListener
	 * @return Controller
	 */
	public T addListener(final ControlListener theListener) {
		_myControlListener.add(theListener);
		return me;
	}

	/**
	 * @see controlP5.ControlListener
	 * @param theListener ControlListener
	 * @return Controller
	 */
	public T removeListener(final ControlListener theListener) {
		_myControlListener.remove(theListener);
		return me;
	}

	/**
	 * @return int
	 */
	public int listenerSize() {
		return _myControlListener.size();
	}

	/**
	 * @see CallbackListener
	 * @param theListener
	 * @return Controller
	 */
	public T addCallback(CallbackListener theListener) {
		cp5.addCallback(theListener, this);
		return me;
	}

	/**
	 * @see CallbackListener
	 * @param theListener
	 * @return Controller
	 */
	public T removeCallback(CallbackListener theListener) {
		cp5.removeCallback(theListener);
		return me;
	}

	/**
	 * @return Controller
	 */
	public T removeCallback() {
		cp5.removeCallback(this);
		return me;
	}

	protected void broadcast() {
		broadcast(_myBroadcastType);
	}

	protected void broadcast(int theType) {
		theType = _myBroadcastType;
		final ControlEvent myEvent = new ControlEvent(this);
		for (ControlListener cl : _myControlListener) {
			cl.controlEvent(myEvent);
		}
		if (isBroadcast && isInit) {
			cp5.getControlBroadcaster().broadcast(myEvent, theType);
			cp5.getControlBroadcaster().invokeAction(new CallbackEvent(this, ControlP5.ACTION_BROADCAST));
		}
		isInit = true;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout @Override public boolean isVisible() {
		if (getParent() != null) {
			if (getParent().isVisible() == false) {
				return false;
			}
		}
		return isVisible;
	}

	/**
	 * @param theFlag boolean
	 * @return Controller
	 */
	@ControlP5.Layout public T setVisible(final boolean theFlag) {
		isVisible = theFlag;
		if (theFlag == false) {
			isActive = false;
		}
		return me;
	}

	/**
	 * @return Controller
	 */
	@Override public T hide() {
		setMouseOver(false);
		isVisible = false;
		isActive = false;
		return me;
	}

	/**
	 * @return Controller
	 */
	@Override public T show() {
		isVisible = true;
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public T setColor(CColor theColor) {
		color.set(theColor);
		setColorCaptionLabel(theColor.getCaptionLabel());
		setColorValueLabel(theColor.getValueLabel());
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Layout public T setColorActive(final int theColor) {
		color.setActive(theColor);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Layout public T setColorForeground(final int theColor) {
		color.setForeground(theColor);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @ControlP5.Layout public T setColorBackground(final int theColor) {
		color.setBackground(theColor);
		return me;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param theColor
	 * @return Controller
	 */
	@ControlP5.Layout public T setColorCaptionLabel(final int theColor) {
		color.setCaptionLabel(theColor);
		_myCaptionLabel.setColor(color.getCaptionLabel());
		return me;
	}

	/**
	 * @param theColor
	 * @return Controller
	 */
	@ControlP5.Layout public T setColorValueLabel(final int theColor) {
		color.setValueLabel(theColor);
		if (_myValueLabel != null) {
			_myValueLabel.setColor(color.getValueLabel());
		}
		return me;
	}

	/**
	 * by default controllers use simple shapes, to replace these shapes with images, use setImages(). This can be handy for buttons,
	 * toggles, bangs, for more complex controllers such as sliders, range, dropdownlist this is not advisable.
	 * 
	 * @param theImageDefault
	 * @param theImageOver
	 * @param theImageActive
	 * @return Controller
	 */
	public T setImages(PImage theImageDefault, PImage theImageOver, PImage theImageActive) {
		setImage(theImageDefault, DEFAULT);
		setImage(theImageOver, OVER);
		setImage(theImageActive, ACTIVE);
		setImage(theImageActive, HIGHLIGHT);
		return me;
	}

	public T setImages(PImage theImageDefault, PImage theImageOver, PImage theImageActive, PImage theImageHighlight) {
		setImage(theImageDefault, DEFAULT);
		setImage(theImageOver, OVER);
		setImage(theImageActive, ACTIVE);
		setImage(theImageHighlight, HIGHLIGHT);
		return me;
	}

	public T setImages(PImage... imgs) {
		if (imgs.length < 3 || imgs.length > 4) {
			return me;
		}
		setImage(imgs[0], DEFAULT);
		setImage(imgs[1], OVER);
		setImage(imgs[2], ACTIVE);
		setImage(imgs.length == 3 ? imgs[2] : imgs[3], HIGHLIGHT);
		return me;
	}

	public T setImage(PImage theImage) {
		return setImage(theImage, DEFAULT);
	}

	/**
	 * @param theImage
	 * @param theState use Controller.DEFAULT (background) Controller.OVER (foreground) Controller.ACTIVE (active)
	 */
	public T setImage(PImage theImage, int theState) {
		if (theImage != null) {
			images[theState] = theImage;
			availableImages[theState] = true;
			updateDisplayMode(IMAGE);
		}
		return me;
	}

	public T updateSize() {
		if (images[DEFAULT] != null) {
			setSize(images[DEFAULT]);
		}
		return me;
	}

	/**
	 * auto-updates the size of a controller according to the dimensions of the PImage.
	 * 
	 * @param theImage
	 * @return Controller
	 */
	public T setSize(PImage theImage) {
		if (theImage != null) {
			setSize(theImage.width, theImage.height);
		}
		return me;
	}

	/**
	 * @param theWidth
	 * @param theHeight
	 * @return Controller
	 */
	public T setSize(int theWidth, int theHeight) {
		setWidth(theWidth);
		setHeight(theHeight);
		return me;
	}

	protected T updateDisplayMode(int theMode) {
		if (theMode != DEFAULT) {
			ControlP5.logger().warning("Image-based or custom displays are not yet implemented for this type of controller. (" + this.getClass().getName() + ")");
		}
		return me;
	}

	/**
	 * use setDisplay to customize your controller look. A new controller-display class required to implement interface ControllerView. By
	 * default the display mode will be set to CUSTOM when setting a new display.
	 * 
	 * @see controlP5.ControllerView
	 * @param theDisplay
	 * @return Controller
	 */
	public T setView(ControllerView<T> theDisplay) {
		setView(theDisplay, CUSTOM);
		return me;
	}

	public void setView(ControllerView<T> theDisplay, int theMode) {
		_myDisplayMode = theMode;
		_myControllerView = theDisplay;
	}

	/**
	 * @deprecated
	 * @exclude
	 */
	@SuppressWarnings("unchecked") @Deprecated public T setDisplay(ControllerDisplay theDisplay) {
		return setView(theDisplay);
	}

	/**
	 * @deprecated
	 * @exclude
	 */
	@SuppressWarnings("unchecked") @Deprecated public void setDisplay(ControllerDisplay theDisplay, int theMode) {
		setView(theDisplay, theMode);
	}

	/**
	 * @see controlP5.Label
	 * @return Label
	 */
	@ControlP5.Layout public Label getCaptionLabel() {
		return _myCaptionLabel;
	}

	/**
	 * @return Label
	 */
	@ControlP5.Layout public Label getValueLabel() {
		return _myValueLabel;
	}

	/**
	 * returns the maximum value of the controller.
	 * 
	 * @return float
	 */
	@ControlP5.Layout public float getMax() {
		return _myMax;
	}

	/**
	 * returns the minimum value of the controller.
	 * 
	 * @return float
	 */
	@ControlP5.Layout public float getMin() {
		return _myMin;
	}

	/**
	 * sets the minimum value of the Controller.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout public T setMin(float theValue) {
		_myMin = theValue;
		changeValue(getValue());
		return me;
	}

	/**
	 * sets the maximum value of the Controller.
	 * 
	 * @param theValue float
	 * @return Controller
	 */
	@ControlP5.Layout public T setMax(float theValue) {
		_myMax = theValue;
		changeValue(getValue());
		return me;
	}

	/**
	 * @param theWidth
	 * @return Controller
	 */
	@ControlP5.Layout public T setWidth(int theWidth) {
		width = theWidth;
		return me;
	}

	/**
	 * @param theHeight
	 * @return Controller
	 */
	@ControlP5.Layout public T setHeight(int theHeight) {
		height = theHeight;
		return me;
	}

	/**
	 * @return int
	 */
	@ControlP5.Layout public int getWidth() {
		return width;
	}

	/**
	 * @return int
	 */
	@ControlP5.Layout public int getHeight() {
		return height;
	}

	/**
	 * sets the decimal precision of a controller's float value displayed. the precision does not apply to the returned float value.
	 * 
	 * @param theValue
	 * @return Controller
	 */
	@ControlP5.Layout public T setDecimalPrecision(int theValue) {
		_myDecimalPoints = theValue;
		// TODO transfer color values of existing label
		int[] t = _myValueLabel.getAlign();
		_myValueLabel = new Label(cp5, "" + (((adjustValue(_myMax)).length() > (adjustValue(_myMin)).length()) ? adjustValue(_myMax) : adjustValue(_myMin)));
		_myValueLabel.align(t);
		_myValueLabel.setColor(color.getValueLabel());
		_myValueLabel.set("" + adjustValue(getValue()));
		return me;
	}

	/**
	 * @return int
	 */
	@ControlP5.Layout public int getDecimalPrecision() {
		return _myDecimalPoints;
	}

	/**
	 * @param theValue float
	 * @return String
	 */
	protected String adjustValue(final float theValue) {
		return adjustValue(theValue, _myDecimalPoints);
	}

	/**
	 * @param theValue
	 * @param theFloatPrecision
	 * @return String
	 */
	protected String adjustValue(final float theValue, final int theFloatPrecision) {

		int myFloatNumberLength = theFloatPrecision + 1;
		if (getControllerPlugList().size() > 0) {
			for (ControllerPlug cp : getControllerPlugList()) {
				if (cp.getClassType() == int.class) {
					myFloatNumberLength = 0;
				}
			}
		}
		String myLabelValue = "" + theValue;
		int myIndex = myLabelValue.indexOf('.');
		if (myIndex > 0) {

			if (theFloatPrecision == 0) {
				myIndex--;
			}
			myLabelValue = myLabelValue.substring(0, (int) Math.min(myLabelValue.length(), myIndex + myFloatNumberLength));

			final int n = (myLabelValue.length() - myIndex);
			if (n < myFloatNumberLength) {
				for (int i = 0; i < myFloatNumberLength - n; i++) {
					myLabelValue += "0";
				}
			}
		} else {
			myLabelValue += ".";
			for (int i = 0; i < myFloatNumberLength; i++) {
				myLabelValue += "0";
			}
		}

		return myLabelValue;
	}

	public T align(int theCaptionX, int theCaptionY, int theValueX, int theValueY) {
		getCaptionLabel().align(theCaptionX, theCaptionY);
		getCaptionLabel().align(theValueX, theValueY);
		return me;
	}

	/**
	 * @return ControlWindow
	 */
	public ControlWindow getControlWindow() {
		return _myControlWindow;
	}

	/**
	 * disables the controller to be moved, or changed or controlled by the user.
	 * 
	 * @return Controller
	 */
	public T lock() {
		isLock = true;
		return me;
	}

	/**
	 * enables the controller to be moved, changed and controlled by the user.
	 * 
	 * @return Controller
	 */
	public T unlock() {
		isLock = false;
		return me;
	}

	/**
	 * @return boolean
	 */
	@ControlP5.Layout public boolean isLock() {
		return isLock;
	}

	/**
	 * sets the lock status of the controller
	 * 
	 * @param theValue
	 * @return Controller
	 */
	@ControlP5.Layout public T setLock(boolean theValue) {
		isLock = theValue;
		return me;
	}

	/**
	 * @exclude TODO
	 */
	@ControlP5.Invisible public T setUserInteraction(boolean theValue) {
		isUserInteraction = theValue;
		return me;
	}

	/**
	 * @exclude
	 * @return boolean
	 */
	@ControlP5.Invisible public boolean isUserInteraction() {
		return isUserInteraction;
	}

	/**
	 * adds a tooltip to a controller, by default the tooltip is disabled. A Tooltip is made visible when entering a controller with the
	 * mouse, when the mouse is moved inside the controller, the tooltip will hide.
	 * 
	 * @param theText
	 * @return Controller
	 */
	public T registerTooltip(String theText) {
		cp5.getTooltip().register(this, theText);
		return me;
	}

	/**
	 * @see controlP5.Controller#registerTooltip(String)
	 * @return Controller
	 */
	public T unregisterTooltip() {
		cp5.getTooltip().unregister(this);
		return me;
	}

	protected T setTooltipEnabled(boolean theValue) {
		tooltipEnabled = theValue;
		return me;
	}

	protected boolean isTooltipEnabled() {
		return tooltipEnabled;
	}

	/**
	 * @return Controller
	 */
	public T linebreak() {
		cp5.linebreak(this, true, autoWidth, autoHeight, autoSpacing);
		return me;
	}

	class DebugView implements ControllerView<T> {

		public void display(PApplet theApplet, T theController) {
			if (inside()) {
				theApplet.fill(255, 0, 0, 50);
				theApplet.stroke(255, 0, 0);
			} else {
				theApplet.fill(255, 50);
				theApplet.stroke(255);
			}

			theApplet.pushMatrix();
			// theApplet.translate(position.x, position.y);
			theApplet.rect(0, 0, width, height);
			theApplet.popMatrix();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public ControllerProperty getProperty(String thePropertyName) {
		return cp5.getProperties().getProperty(this, thePropertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public ControllerProperty getProperty(String theSetter, String theGetter) {
		return cp5.getProperties().getProperty(this, theSetter, theGetter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public T registerProperty(String thePropertyName) {
		cp5.getProperties().register(this, thePropertyName);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public T registerProperty(String theSetter, String theGetter) {
		cp5.getProperties().register(this, theSetter, theGetter);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public T removeProperty(String thePropertyName) {
		cp5.getProperties().remove(this, thePropertyName);
		return me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public T removeProperty(String theSetter, String theGetter) {
		cp5.getProperties().remove(this, theSetter, theGetter);
		return me;
	}

	/**
	 * @exclude {@inheritDoc}
	 */
	@Override public String toString() {
		return getName() + " [" + getClass().getSimpleName() + "]";
	}

	/**
	 * @exclude
	 * @return String
	 */
	public String getInfo() {
		return "[ type:\tController" + "\nname:\t" + _myName + "\n" + "label:\t" + _myCaptionLabel.getText() + "\n" + "id:\t" + _myId + "\n" + "value:\t" + getValue() + "\n" + "arrayvalue:\t"
				+ CP.arrayToString(_myArrayValue) + "\n" + "position:\t" + position + "\n" + "absolute:\t" + absolutePosition + "\n" + "width:\t" + getWidth() + "\n" + "height:\t" + getHeight()
				+ "\n" + "color:\t" + getColor() + "\n" + "visible:\t" + isVisible + "\n" + "moveable:\t" + isMoveable + " ]";
	}

	/* Deprecated methods */

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public PVector position() {
		return getPosition();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public PVector absolutePosition() {
		return getAbsolutePosition();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public void setWindow(final PApplet theApplet) {
		moveTo(theApplet);
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public ControlWindow setWindow(final ControlWindow theWindow) {
		moveTo(theWindow);
		return _myControlWindow;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public void trigger() {
		setValue(value());
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public T setColorLabel(final int theColor) {
		setColorCaptionLabel(theColor);
		return me;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public T setColorValue(final int theColor) {
		setColorValueLabel(theColor);
		return me;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public float value() {
		return getValue();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public String stringValue() {
		return getStringValue();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public float[] arrayValue() {
		return getArrayValue();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public float min() {
		return getMin();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public float max() {
		return getMax();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public CColor color() {
		return color;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public ControlBehavior behavior() {
		return getBehavior();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public Label captionLabel() {
		return getCaptionLabel();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public float defaultValue() {
		return getDefaultValue();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public ControlWindow controlWindow() {
		return _myControlWindow;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public String label() {
		return _myCaptionLabel.toString();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public T listen() {
		listen(true);
		return me;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public String name() {
		return getName();
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public int id() {
		return _myId;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public T setLabel(final String theLabel) {
		setCaptionLabel(theLabel);
		return me;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public Label valueLabel() {
		return _myValueLabel;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public T setAutoUpdate(boolean theValue) {
		listen(theValue);
		return me;
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated protected ControllerPlug controllerPlug(int theIndex) {
		return _myControllerPlugList.get(theIndex);
	}

	/**
	 * @exclude
	 * @deprecated
	 */
	@Deprecated public ControllerInterface<?> parent() {
		return _myParent;
	}

}