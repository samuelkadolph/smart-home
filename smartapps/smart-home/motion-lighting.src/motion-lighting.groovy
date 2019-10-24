/*
 *  Motion Lighting
 *    SmartThings SmartApp that turns lights on and off based on a motion sensor.
 *
 *  Copyright (c) 2019 Samuel Kadolph
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

definition(
  namespace: "smart-home",
  name: "Motion Lighting",
  author: "Samuel Kadolph",
  description: "Automatically control lights with motion sensors",
  category: "Convenience",
  iconUrl: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn.png",
  iconX2Url: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn@2x.png",
  iconX3Url: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn@3x.png"
)

preferences {
  section("Turn on and off these lights") {
    input "lights", "capability.switch", title: "Which lights?", multiple: true
  }

  section("When there is motion detected by") {
    input "sensors", "capability.motionSensor", title: "Which sensors?", multiple: true
  }
}

def installed() {
  log.debug("installed() ${settings}")

  initialize()
}

def handleMotionEvent(event) {
  log.debug("handleMotionEvent(value:${event.value}, data:${event.data}")

  if (event.value == "active") {
  }
}

def handleSwitchEvent(event) {
  log.debug("handleSwitchEvent(value:${event.value}, data:${event.data}")
}

def updated() {
  log.debug("updated() ${settings}")

  unsubscribe()
  initialize()
}

private def initialize() {
  subscribe(lights, "switch", handleSwitchEvent)
  subscribe(sensors, "motion", handleMotionEvent)

  if (!state.lights) {
    state.lights = []
  }
}
