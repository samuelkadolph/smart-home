/**
 *  Shade Control
 *    SmartThings SmartApp that controls window shades with the buttons on a switch.
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
  name: "Shade Control",
  namespace: "smart-home",
  author: "Samuel Kadolph",
  description: "Control window shades with wall switches",
  category: "Convenience",
  iconUrl: "http://cdn.device-icons.smartthings.com/Home/home9-icn.png",
  iconX2Url: "http://cdn.device-icons.smartthings.com/Home/home9-icn@2x.png",
  iconX3Url: "http://cdn.device-icons.smartthings.com/Home/home9-icn@3x.png"
)

preferences {
  section("Shades to control") {
    input "shades", "capability.windowShade", multiple: true
  }

  section("Using these switches") {
    input "switches", "capability.switch", multiple: true
  }
}

def installed() {
  log.debug("installed() settings: ${settings}")

  initialize()
}

def updated() {
  log.debug("updated() settings: ${settings}")

  unsubscribe()
  initialize()
}

def handleButtonEvent(event) {
  def data = new groovy.json.JsonSlurper().parseText(event.data)

  log.debug("handleButtonEvent() event: ${event.value} ${data.buttonNumber}")

  if (event.value == "pushed") {
    if (data.buttonNumber == 5) {
      log.debug("Opening shades ${shades}")
      shades.on()
    } else if (data.buttonNumber == 6) {
      log.debug("Closing shades ${shades}")
      shades.off()
    }
  }
}

private def initialize() {
  subscribe(switches, "button", handleButtonEvent)
}