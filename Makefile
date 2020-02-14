CONVERTBIN ?= convert

ICONS := $(basename $(wildcard icons/*.svg))

default: all

define BUILD_ICON_RULE
$(i).png: $(i).svg
	convert -density 1200 -resize 49x49 $(i).svg $(i).png

$(i)@2x.png: $(i).svg
	convert -density 1200 -resize 98x98 $(i).svg $(i)@2x.png

$(i)@3x.png: $(i).svg
	convert -density 1200 -resize 150x150 $(i).svg $(i)@3x.png
endef
$(foreach i, $(ICONS), $(eval $(BUILD_ICON_RULE)))

all: $(addsuffix .png, $(ICONS)) $(addsuffix @2x.png, $(ICONS)) $(addsuffix @3x.png, $(ICONS))

clean:
	rm -f icons/*.png

.PHONY: all clean
