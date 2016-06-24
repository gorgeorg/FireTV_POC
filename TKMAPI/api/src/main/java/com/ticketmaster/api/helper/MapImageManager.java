package com.ticketmaster.api.helper;

import com.ticketmaster.api.discovery.PromoDiscoveryAPI;

/**
 * Created by Georgii_Goriachev on 5/20/2016.
 */
public class MapImageManager {
    private String key;
    private String center;
    private int zoom;
    private String size;
    private Metatype maptype;

    enum Metatype {
        roadmap, satellite, hybrid, terrain;
    }

    private MapImageManager() {
    }

    private MapImageManager(String center, Metatype maptype, String size, int zoom) {
        this.center = center;
        this.maptype = maptype;
        this.size = size;
        this.zoom = zoom;
    }

    public String getMapUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/staticmap?center=")
                .append(center)
                .append("&zoom=")
                .append(zoom)
                .append("&size=")
                .append(size)
                .append("&maptype=")
                .append(this.maptype.name())
                .append("&markers=color:red%7Clabel:E%7C")
                .append(center)
                .append("&key=")
                .append(PromoDiscoveryAPI.STATIC_MAP_API_KEY);
        return sb.toString();
    }


    public static class Builder {
        private String center;
        private int zoom = 12;
        private String size = "1920x1080";
        private Metatype maptype = Metatype.roadmap;

        public MapImageManager build() {
            if (center == null) {
                throw new IllegalArgumentException("setCenter is missing");
            }
            return new MapImageManager(this.center, this.maptype, this.size, this.zoom);
        }

        public Builder setCenter(String center) {
            this.center = center;
            return this;
        }

        public Builder setMaptype(Metatype maptype) {
            this.maptype = maptype;
            return this;
        }

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder setZoom(int zoom) {
            this.zoom = zoom;
            return this;
        }

    }
}
