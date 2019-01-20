/*
 *  Lights On Your Return
 *    SmartThings SmartApp that turns lights on and off after a delay when you get home.
 *
 *  Copyright (c) 2018 Samuel Kadolph
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
  name: "Lights On Your Return",
  namespace: "smart-home",
  author: "Samuel Kadolph",
  description: "Todo",
  category: "Safety & Security",
  iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld.png",
  iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld@2x.png",
  iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld@2x.png"
)

preferences {
  section("When one of these people comes home") {
    input "people", "capability.presenceSensor", multiple: true
  }

  section("Turn the following lights on") {
    input "lights", "capability.switch", multiple: true
  }

  section("And turn the lights off after") {
    input "delay", "decimal", title: "Number of minutes", required: false
  }
}

def installed() {
  log.debug("installed() settings: ${settings}")

  initialize()
}

def presence(event) {
  log.debug("presence(${event}")
}

def updated() {
  log.debug("updated() settings: ${settings}")

  unsubscribe()
  initialize()
}

private def initialize() {
  subscribe(people, "presence", presence)
}
