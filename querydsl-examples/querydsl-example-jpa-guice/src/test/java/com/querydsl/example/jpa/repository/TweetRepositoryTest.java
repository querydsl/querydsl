package com.querydsl.example.jpa.repository;

import com.google.common.collect.Lists;
import com.querydsl.example.jpa.model.Tweet;
import com.querydsl.example.jpa.model.User;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.querydsl.example.jpa.model.QTweet.tweet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TweetRepositoryTest extends AbstractPersistenceTest {
    @Inject
    private TweetRepository repository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void save_and_find_by_id() {
        User poster = new User("dr_frank");
        userRepository.save(poster);

        String content = "I am alive! #YOLO";
        Tweet tweet = new Tweet(poster, content,
                Collections.<User>emptyList(), null);
        repository.save(tweet);
        assertEquals(content, repository.findById(tweet.getId()).getContent());
    }

    @Test
    public void find_list_by_predicate() {
        User poster = new User("dr_frank");
        userRepository.save(poster);

        repository.save(new Tweet(poster, "It is a alive! #YOLO", Collections.<User>emptyList(), null));
        repository.save(new Tweet(poster, "Oh the humanity!", Collections.<User>emptyList(), null));
        repository.save(new Tweet(poster, "#EpicFail", Collections.<User>emptyList(), null));
        assertEquals(1, repository.findAll(tweet.content.contains("#YOLO")).size());
    }

    @Test
    public void find_list_by_predicate_with_hibernate() {
        User poster = new User("dr_frank");
        userRepository.save(poster);

        repository.save(new Tweet(poster, "It is a alive! #YOLO", Collections.<User>emptyList(), null));
        repository.save(new Tweet(poster, "Oh the humanity!", Collections.<User>emptyList(), null));
        repository.save(new Tweet(poster, "#EpicFail", Collections.<User>emptyList(), null));
        assertEquals(1, repository.findAllWithHibernateQuery(tweet.content.contains("#YOLO")).size());
    }


    @Test
    public void find_list_by_complex_predicate() {
        List<String> usernames = Lists.newArrayList("dr_frank", "mike", "maggie", "liza");
        List<User> users = Lists.newArrayList();
        for (String username : usernames) {
            users.add(userRepository.save(new User(username)));
        }
        User poster = new User("duplo");
        userRepository.save(poster);
        for (int i = 0; i < 100; i++) {
            repository.save(new Tweet(poster, "spamming @dr_frank " + i, users.subList(0, 1), null));
        }
        assertTrue(repository.findAll(tweet.mentions.contains(users.get(1))).isEmpty());

        assertEquals(100, repository.findAll(tweet.mentions.contains(users.get(0))).size());

        assertTrue(repository.findAll(tweet.mentions.any().username.eq("duplo")).isEmpty());

        assertEquals(100, repository.findAll(tweet.mentions.any().username.eq("dr_frank")).size());
    }

    @Test
    public void find_list_by_complex_predicate_hibernate() {
        List<String> usernames = Lists.newArrayList("dr_frank", "mike", "maggie", "liza");
        List<User> users = Lists.newArrayList();
        for (String username : usernames) {
            users.add(userRepository.save(new User(username)));
        }
        User poster = new User("duplo");
        userRepository.save(poster);
        for (int i = 0; i < 100; i++) {
            repository.save(new Tweet(poster, "spamming @dr_frank " + i, users.subList(0, 1), null));
        }
        assertTrue(repository.findAllWithHibernateQuery(tweet.mentions.contains(users.get(1))).isEmpty());

        assertEquals(100, repository.findAllWithHibernateQuery(tweet.mentions.contains(users.get(0))).size());

        assertTrue(repository.findAllWithHibernateQuery(tweet.mentions.any().username.eq("duplo")).isEmpty());

        assertEquals(100, repository.findAllWithHibernateQuery(tweet.mentions.any().username.eq("dr_frank")).size());
    }
}
