About Chest GUI
=====

A chest GUI is basically a dynamic menu that uses items and the standard chest layout to present a list of options in game.

These items cannot be removed from the chest and the click event is instead passed on as a ChestGUIEvent. Because the mod requesting this might not be on the client, the event is sent in both locations by utilizing a network packet. 



ChestGUIEvent
====

This event is the parent of several other events.

OptionInteractEvent
----
This event gets dispatched on both the client and server when a option is interacted with. 

OptionUpdateEvent
----
To be sent by the mod originating this dynamic menu. This event will instruct the ChestGUI to update a item, or multiple items after a interaction, or something else occuring. If the GUI is not open, this event gets ignored. This event should only be sent when we know the GUI is actually open!