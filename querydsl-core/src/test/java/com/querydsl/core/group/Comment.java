/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.group;

public class Comment {

    private Integer id;

    private String text;

    private Double score;

    public Comment() { }

    public Comment(Integer id, String text, Double score) {
        this.id = id;
        this.text = text;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + text.hashCode() + score.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Comment) {
            Comment other = (Comment) o;
            return this.id.equals(other.id) && this.text.equals(other.text) && this.score.equals(other.score);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return id + ": " + text + "(score: " + score + ")";
    }
}