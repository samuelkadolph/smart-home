CONVERT ?= convert

ICONS := $(basename $(notdir $(wildcard icons/src/*.svg)))


default: all

define BUILD_ICON_RULE
icons/$(i).png: icons/src/$(i).svg
	$(CONVERT) -density 1200 -resize 49x49 icons/src/$(i).svg icons/$(i).png

icons/$(i)@2x.png: icons/src/$(i).svg
	$(CONVERT) -density 1200 -resize 98x98 icons/src/$(i).svg icons/$(i)@2x.png

icons/$(i)@3x.png: icons/src/$(i).svg
	$(CONVERT) -density 1200 -resize 150x150 icons/src/$(i).svg icons/$(i)@3x.png

all: icons/$(i).png icons/$(i)@2x.png icons/$(i)@3x.png
endef
$(foreach i, $(ICONS), $(eval $(BUILD_ICON_RULE)))

clean:
	rm -f icons/*.png

.PHONY: all clean
