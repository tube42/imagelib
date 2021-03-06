imagelib
========

The tube42 imagelib is a minimal image manipulation library for J2ME phones.
It was initially published on my blog in a series of articles about lightweight image processing. Those posts are now found on the wiki:


* Part 1: `Basic RGB image manipulation`_
* Part 2: `​Image resizing`_
* Part 3: `Performance enhancement and Blackberry code`_
* Part 4: `Image effects`_
* Part 5: `Performance of transparent vs. opaque blit`_
* Part 6: `Updates to the filter and pixel modifiers + 2D filtering support`_

.. _Basic RGB image manipulation: https://github.com/tube42/imagelib/wiki/Part1
.. _​Image resizing: https://github.com/tube42/imagelib/wiki/Part2
.. _Performance enhancement and Blackberry code: https://github.com/tube42/imagelib/wiki/Part3
.. _Image effects: https://github.com/tube42/imagelib/wiki/Part4
.. _Performance of transparent vs. opaque blit: https://github.com/tube42/imagelib/wiki/Part5
.. _Updates to the filter and pixel modifiers + 2D filtering support: https://github.com/tube42/imagelib/wiki/Part6

Currently, the library contains functions for color operations including interpolation, blending and RGB/YCbCr conversion, image operations including resizing, blending, filtering and pixel modifiers and some simple image analysis functions.

Great, but what is it for?
--------------------------
You can for example use the imagelib to

* dynamically resize your graphics, depending on the detected screen size
* create a "retro effect" application

What is it NOT good for?
------------------------

* J2ME is slow and so are most J2ME phones. Given that image processing is very CPU intensive, this library is NOT suitable for real-time effects and such.

Examples
--------

The source code contains a MIDlet sample project that demonstrates many aspects of the library.
The following images were generated by the sample project running on a phone:

.. image:: https://github.com/tube42/imagelib/wiki/img/sepia.png

.. image:: https://github.com/tube42/imagelib/wiki/img/retro.png

.. image:: https://github.com/tube42/imagelib/wiki/img/hieq.png

.. image:: https://github.com/tube42/imagelib/wiki/img/color.png
