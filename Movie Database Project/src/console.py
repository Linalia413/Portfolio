from datetime import date
from datetime import datetime
import time
today_date = date.today()
today_time = datetime.now()
date_formatted = today_date.strftime('%Y-%m-%d')
time_formatted = today_time.strftime('%H:%M:%S')


def process_commands(user_input, user_Id, conn):
    """
    Processes command from input
    Checks for proper input of command otherwise sends error message
    :param user_input: user input from command line
    :return: bool
    """
    if user_input[0] == "!createcollection":
        create_collection(user_input, user_Id, conn)

    elif user_input[0] == "!mycollections":
        display_my_collections(user_input, user_Id, conn)

    elif user_input[0] == "!search":
        search(user_input, user_Id, conn)

    elif user_input[0] == "!sort":
        sort(user_input, user_Id, conn)

    elif user_input[0] == "!addmovie":
        add_movie(user_input, user_Id, conn)

    elif user_input[0] == "!deletemovie":
        user_input(user_input, user_Id, conn)

    elif user_input[0] == "!modifycollection":
        modify_collection(user_input, user_Id, conn)

    elif user_input[0] == "!rate":
        rate(user_input, user_Id, conn)

    elif user_input[0] == "!playmovie":
        play_movie(user_input, user_Id, conn)

    elif user_input[0] == "!playcollection":
        play_collection(user_input, user_Id, conn)

    elif user_input[0] == "!findfriend":
        find_friend(user_input, user_Id, conn)

    elif user_input[0] == "!follow":
        follow(user_input, user_Id, conn)

    elif user_input[0] == "!unfollow":
        unfollow(user_input, user_Id, conn)

    elif user_input[0] == "!deletecollection":
        delete_collection(user_input, user_Id, conn)

    elif user_input[0] == "!displaycollections":
        display_my_collections(user_input, user_Id, conn)

    elif user_input[0] == "!displayprofile":
        display_user_profile(user_input, user_Id, conn)

    elif user_input[0] == "!top20movies":
        top_20_last_90(user_input, user_Id, conn)

    elif user_input[0] == "!top20moviesfriends":
        top_20_friends(user_input, user_Id, conn)

    elif user_input[0] == "!last5movies":
        last_5_releases(user_input, user_Id, conn)

    elif user_input[0] == "!myrecommendations":
        recommended_movies(user_input, user_Id, conn)

    elif user_input[0] == "!exit":
        return True

    else:
        print("Illegal command usage, please try again. \n")

def help():
    """
    Prints out the command list to the user
    :return:
    """
    print("!help - prints out the command list to the user, brings up this screen.")
    print("!exit - exits the application.")
    print("!createacc - create a new account USAGE: !createacc [username] [password] [firstname] [lastname] [email]")
    print("\n")
    print("Must be logged in for the following commands.")
    print("!createcollection - creates a collection of movies USAGE !createcollection [collection_name]")
    print("!mycollections - displays user's collections.")
    print("!search - searches movies. USAGE: !search [criteria] [value]!")
    print("!sort - sorts all movies by user specification. USAGE !sort [criteria] [asc/desc].")
    print("!addmovie - add movie to collection. USAGE !addmovie [movie_name] [collection_name].")
    print("!deletemovie - deletes movie from collection. USAGE !deletemovie [movie_name] [collection_name].")
    print("!modifycollection - changes a collection's name. USAGE !modifycollection [old_name] [new_name].")
    print("!rate - gives a rating to a movie. USAGE !rate [movie_name] [rating].")
    print("!playmovie - plays a movie. USAGE !playmovie [movie_name].")
    print("!playcollection - plays a collection of movies. USAGE !playcollection [collection_name].")
    print("!findfriend - lists all available emails. USAGE !findfriend [email].")
    print("!follow - follows a friend. USAGE !follow [email].")
    print("!unfollow - unfollows a friend. USAGE !unfollow [email].")
    print("!deletecollection - deletes an entire collection. USAGE !deletecollection [collection_name]")
    print("!displaycollections - displays all collections; shows name, number of movies in each collection, "
          "total length of movies in collection. USAGE !displaycollections")
    print("!displayprofile - displays user profile; shows number of collections, number of followers, number of "
          "following, top 10 movies by rating. USAGE !displayprofile")


def login(conn):  # COMPLETELY FINISHED DON'T TOUCH
    """
    Attempts to log the user into the database.
    Asks users for a username and password and checks if they match in the database.
    :param conn: the connection to the sql server
    :return: the user_Id of the user if login is successful, -1 if not
    """
    curs = conn.cursor()
    userInput = input("Please enter your username.\n").split(" ")
    # do basic commands
    curs.execute("SELECT username FROM users WHERE username = %s;", (userInput[0],))
    if curs.fetchone() is not None:  # connect to sql server and check if username exists
        password = input("Please enter your password.\n")
        curs.execute("SELECT password FROM users WHERE password = %s;", (password,))
        if curs.fetchone() is not None:  # connect to to sql server and check if password is correct
            curs.execute("SELECT user_id FROM users WHERE password = %s;", (password,))
            userId = curs.fetchone()[0]
            today_date = date.today()
            today_time = datetime.now()
            date_formatted = today_date.strftime('%Y-%m-%d')
            time_formatted = today_time.strftime('%H:%M:%S')
            curs.execute("UPDATE users SET access_date = %s WHERE user_id = %s;", (date_formatted, userId))
            curs.execute("UPDATE users SET access_time = %s WHERE user_id = %s;", (time_formatted, userId))
            conn.commit()
            return userId
        else:
            print("Password not recognized\n")
            return -1
    else:
        print("That username is not recognized\n")
        return -1


def create_acc(user_input, conn):  # COMPLETELY FINISHED DON'T TOUCH
    """
    Creates a new user account based on user input
    :param user_input: The full command the user inputs.
    usage: !createacc [username] [password] [firstname] [lastname] [email]
    :param conn: the connection to the database
    :return: True if account was created successfully, False otherwise
    """
    curs = conn.cursor()
    if (len(user_input) != 6):
        print("Incorrect number of statements, command aborted\n")
        return False
    curs.execute("SELECT username FROM users WHERE (username = %s OR email = %s);", (user_input[1], user_input[5]))
    if curs.fetchone() is None:  # replace true with checking for duplicate usernames
        sql = "INSERT INTO users (username, user_id, password, firstname, lastname, access_date, email, creation_date, access_time, creation_time) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
        curs.execute("SELECT COUNT(username) FROM users")
        ID = curs.fetchone()[0]
        curs.execute(sql, (
        user_input[1], ID, user_input[2], user_input[3], user_input[4], today_date, user_input[5], date_formatted,
        time_formatted, time_formatted))
        print("created account " + user_input[1])  # add account and password to database
        conn.commit()
        return True
    else:
        print("account not created, username or email already exists\n")
        return False


def create_collection(user_input, user_Id, conn):
    """
    Creates a new collection and ties it to the current user
    Users must be logged in to use this command
    :param user_input: The full command the user inputs
    usage: !createcollection [collection_name]
    :param user_Id: the current user's user_Id
    :param conn: the connection to the database
    :return: True if collection created successfully, False otherwise
    """
    curs = conn.cursor()
    if (user_Id < 0):
        print("You must be logged in to do that. Command aborted.\n")
        return False
    if (len(user_input) != 2):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    if user_input[1] == user_input[1]:
        curs.execute("SELECT name FROM collections WHERE name = %s;",
                     (user_input[1],))  # wrong does not allow any collections to have the same name regardless of user
        if curs.fetchone() is None:
            sql = "INSERT INTO collections(collection_id, name) VALUES (%s, %s);"
            curs.execute("SELECT COUNT(collection_id) FROM collections")
            collection_ID = curs.fetchone()[0]
            curs.execute(sql, (collection_ID, user_input[1]))
            print("created collection " + user_input[1])  # add collection to database
            sql2 = "INSERT INTO collectionuser(collection_id, user_id) VALUES (%s, %s);"
            curs.execute(sql2, (collection_ID, user_Id))
            print("added to collectionuser table")
            sql3 = "INSERT INTO collectionmovie(collection_id, movie_id) VALUES (%s, NULL);"
            curs.execute(sql3, (collection_ID,))
            conn.commit()
            return True
        else:
            print("collection not created, collection name already exists\n")
            return False
    else:
        print("collection not created\n")
        return False


def delete_collection(user_input, user_id, conn):
    """
    Deletes a collection from a user
    Users must be logged in to use this command
    :param user_input: The full command the user inputs
    usage: !deletecollection [collection_name]
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True if the collection was deleted successfully, False otherwise
    """
    curs = conn.cursor()
    if (len(user_input) != 2):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    if user_input[1] == user_input[1]:
        curs.execute("DELETE FROM collections WHERE name = %s;",
                     (user_input[1],))  # works in datagrip console but not here
        print("collection " + user_input[1] + " has been deleted\n")
        conn.commit()
        return True


def display_my_collections(user_input, user_Id, conn):
    """
    Displays the user's collections
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if (len(user_input) != 1):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    curs.execute(
        "SELECT * FROM collectionuser cu INNER JOIN collections c on c.collection_id = cu.collection_id WHERE user_id = %s;",
        (user_Id,))
    rows = curs.fetchall()
    if rows is None:
        print("You have no collections.")
        return False
    for row in rows:
        print("Collection name: " + row[3])
    return True


def search(user_input, user_id, conn):
    """
    Search by actor, director, movie name, genre, or release date
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    # movie name, release date, cast memeber, studio, or genre
    curs = conn.cursor()
    if (user_id < 0):
        print("You must be logged in to do that. Command aborted.\n")
        return False
    elif user_input[1] == "actor":
        sql = "SELECT m.title FROM movies m INNER JOIN actormovie am ON m.movie_id = am.movie_id INNER JOIN actors a ON am.person_id = a.actor_id WHERE a.actor LIKE %s GROUP BY m.movie_id ORDER BY m.title ASC;"
        mySearch = "%" + user_input[2] + "%"
        curs.execute(sql, (mySearch,))
        movieslist = curs.fetchall()
        for x in movieslist:
            print(*x)
    elif user_input[1] == "director":
        sql = "SELECT m.title FROM movies m INNER JOIN directormovie dm ON m.movie_id = dm.movie_id INNER JOIN directors d ON dm.person_id = d.director_id WHERE d.director LIKE %s GROUP BY m.movie_id ORDER BY m.title ASC;"
        mySearch = "%" + user_input[2] + "%"
        curs.execute(sql, (mySearch,))
        movieslist = curs.fetchall()
        for x in movieslist:
            print(*x)
    elif user_input[1] == "movie_name":
        sql = "SELECT m.title FROM movies m WHERE m.title LIKE %s ORDER BY m.title ASC;"
        mySearch = "%" + user_input[2] + "%"
        curs.execute(sql, (mySearch,))
        movieslist = curs.fetchall()
        for x in movieslist:
            print(*x)
    elif user_input[1] == "genre":
        sql = "SELECT m.title FROM movies m INNER JOIN genremovies gm ON m.movie_id = gm.movie_id INNER JOIN genres g ON gm.genre_id = g.genre_id WHERE g.genre LIKE %s GROUP BY m.movie_id ORDER BY m.title ASC;"
        mySearch = "%" + user_input[2] + "%"
        curs.execute(sql, (mySearch,))
        movieslist = curs.fetchall()
        for x in movieslist:
            print(*x)
    elif user_input[1] == "release_date":
        sql = "SELECT m.title FROM movies m WHERE m.release_date = %s ORDER BY m.title ASC;"
        mySearch = user_input[2]
        curs.execute(sql, (mySearch,))
        movieslist = curs.fetchall()
        for x in movieslist:
            print(*x)
    else:
        print("Invalid search functionality, please try again with one of the listed options...")


def sort(user_input, user_id, conn):
    """
    Sorts all movies based on genre, studio, or title, ascending or descending
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    curs = conn.cursor()
    if (len(user_input) != 3):
        print("incorrect number of statements, command aborted\n")
        return False
    if user_input[1] == "genre" and user_input[2] == "asc":
        print("Sorting movies by genre in alphabetical order.\n")
        curs.execute("SELECT m.title, g.genre FROM movies m INNER JOIN genremovies gm on m.movie_id = gm.movie_id "
                     "INNER JOIN genres g on gm.genre_id = g.genre_id ORDER BY gm.genre_id ASC;")
        movies = curs.fetchall()
        print("Genre       |Movie\n")
        for movie in movies:
            whitespace = "            "[0:(12 - len(movie[1]))]
            print(movie[1] + whitespace + "|" + movie[0] + "\n")
        return True
    elif user_input[1] == "genre" and user_input[2] == "dsc":
        print("Sorting movies by genre in reverse alphabetical order.\n")
        curs.execute("SELECT m.title, g.genre FROM movies m INNER JOIN genremovies gm on m.movie_id = gm.movie_id "
                     "INNER JOIN genres g on gm.genre_id = g.genre_id ORDER BY gm.genre_id DESC")
        movies = curs.fetchall()
        print("Genre       |Movie\n")
        for movie in movies:
            whitespace = "            "[0:(12 - len(movie[1]))]
            print(movie[1] + whitespace + "|" + movie[0] + "\n")
        return True
    elif user_input[1] == "studio" and user_input == "asc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title, m.studio FROM movies m ORDER BY m.studio ASC")
        movies = curs.fetchall()
        print("Studio                        |Movie\n")
        for movie in movies:
            whitespace = "                              "[0:(30 - len(movie[1]))]
            print(movie[1] + whitespace + "|" + movie[0] + "\n")
        return True
    elif user_input[1] == "studio" and user_input == "dsc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title, m.studio FROM movies m ORDER BY m.studio DESC")
        movies = curs.fetchall()
        print("Studio                        |Movie\n")
        for movie in movies:
            whitespace = "                              "[0:(30 - len(movie[1]))]
            print(movie[1] + whitespace + "|" + movie[0] + "\n")
        return True
    elif user_input[1] == "title" and user_input == "asc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title FROM movies m ORDER BY m.title ASC")
        movies = curs.fetchall()
        print("Movie\n")
        for movie in movies:
            print(movie[0] + "\n")
        return True
    elif user_input[1] == "title" and user_input == "dsc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title FROM movies m ORDER BY m.title DESC")
        movies = curs.fetchall()
        print("Movie\n")
        for movie in movies:
            print(movie[0] + "\n")
        return True
    elif user_input[1] == "year" and user_input == "asc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title, m.release_date FROM movies m ORDER BY m.release_date ASC")
        movies = curs.fetchall()
        print("Date      |Movie\n")
        for movie in movies:
            print(movie[1] + "|" + movie[0] + "\n")
        return True
    elif user_input[1] == "year" and user_input == "dsc":
        print("Sorting movies by studio in alphabetical order.\n")
        curs.execute("SELECT m.title, m.release_date FROM movies m ORDER BY m.release_date DESC")
        movies = curs.fetchall()
        print("Date      |Movie\n")
        for movie in movies:
            print(movie[1] + "|" + movie[0] + "\n")
        return True
    else:
        print("Invalid usage of command '!sort', please try again...\n")
        return False


def add_movie(user_input, user_id, conn):
    """
    Adds a movie to a collection
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if (len(user_input) != 3):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    curs.execute("SELECT title FROM movies WHERE title = %s",
                 (user_input[1],))  # checks if movie exists in movielist works in datagrip console but not here
    title = curs.fetchone()
    if title is None:
        print("Movie not added, movie does not exist\n")
        return False
    curs.execute("SELECT name FROM collections WHERE name = %s",
                 (user_input[2],))  # checks if movie is already in collection
    collection_name = curs.fetchone()
    if collection_name is None:
        # curs.execute("SELECT move_id FROM collectionmovie WHERE movie_id = NULL")
        print("Movie not added, Collection does not exist.\n")
        return False
    if collection_name is not None:
        # sql = "INSERT INTO collectionmovie(collection_id, movie_id) VALUES (%s, %s);"
        # curs.execute(sql, (collection_id, movie_id))
        curs.execute("SELECT title, movie_id from movies WHERE title = %s", (user_input[1],))
        movie_title = curs.fetchone()
        curs.execute("SELECT collection_id from collections where collections.name = %s", (user_input[2],))
        name = curs.fetchone()
        curs.execute("INSERT INTO collectionmovie VALUES(%s, %s)", (name[0], movie_title[1],))
        conn.commit()
        print("Movie added into collection")
        return True
    else:
        print("Movie not added, movie already exists in collection\n")
        return False


def delete_movie(user_input, user_Id, conn):
    """
    Delets a movie from collection
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    curs = conn.cursor()
    if (len(user_input) != 3):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    if user_input[1] == user_input[1]:
        # curs.execute("DELETE FROM collectionmovie WHERE movie_id = '%s';", (user_input[1],))  # join statement to get movie_id from name of movies list
        print("movie " + user_input[1] + " has been deleted from " + user_input[2] + " collection.")
        conn.commit()
        return True
    else:
        print("movie not deleted")
        return False


def modify_collection(user_input, user_id, conn):
    """
    Changes a collection
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if (len(user_input) != 3):
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    curs.execute("SELECT name FROM collections WHERE name = %s;", (user_input[1],))  # breaks here
    collection = curs.fetchall()
    if collection is None:
        print("collection not modified, collection name does not exist\n")
        return False
    elif collection is not None:
        curs.execute("UPDATE collections SET name = %s WHERE name = %s;", (user_input[2], user_input[1],))
        print("collection " + user_input[1] + " has been modified to " + user_input[2] + "\n")
        conn.commit()
        return True
    else:
        print("Collection not modified")
        return False


def rate(user_input, user_Id, conn):
    """
    Rates a movie
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 3:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    # curs.execute("SELECT * FROM usermovie WHERE (movie_id = (SELECT movie_id FROM movies WHERE title = %s) AND user_id = %s);",(user_input[1], user_Id))
    curs.execute("SELECT * FROM usermovie WHERE (movie_id = (SELECT movie_id FROM movies WHERE title = %s));",
                 (user_input[1],))
    movie_user = curs.fetchall()
    if movie_user is not None:
        curs.execute("SELECT movie_id from movies where title = %s", (user_input[1],))
        movie_id = curs.fetchone()
        curs.execute("INSERT INTO usermovie(user_rating, flag, movie_id, user_id) VALUES (%s, %s, %s, %s);",
                     (user_input[2], True, movie_id[0], user_Id))  # get movie_id from user_input[1]
        conn.commit()
        print(user_input[1] + " has been rated successfully")
        return True
    else:
        print("Rating not added")
        return False


def play_movie(user_input, user_Id, conn):
    """
    Plays a movie for a user
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 2:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    curs.execute("SELECT * FROM usermovie WHERE (movie_id = (SELECT movie_id FROM movies WHERE title = %s));",
                 (user_input[1],))
    movie_user = curs.fetchall()
    if movie_user is not None:
        curs.execute("SELECT movie_id from movies where title = %s", (user_input[1],))
        movie_id = curs.fetchone()
        curs.execute("INSERT INTO usermovie(user_rating, flag, movie_id, user_id) VALUES (%s, %s, %s, %s);",
                     (None, True, movie_id[0], user_Id))  # get movie_id from user_input[1]
        conn.commit()
        print(user_input[1] + " has been marked as watched! \n")
        return True
    else:
        print("Rating not added")
        return False
    # curs = conn.cursor()
    # curs.execute("SELECT * FROM usermovie WHERE (movie_id = (SELECT movie_id FROM movies WHERE title = %s) AND user_id = %s);", (user_input[1], user_Id))
    # if(curs.fetchone() == None):
    #   curs.execute("INSERT INTO usermovie(user_rating, flag,  movie_id, user_id) VALUES (%s, %s, (SELECT movie_id FROM movies WHERE title = %s), %s)",(None, True, user_input[1], user_Id))
    #  conn.commit()
    # print("Movie marked as watched\n")
    # return True
    # else:
    #   print("movie not marked as watched\n")
    #  return False

def play_collection(user_input, user_Id, conn):
    """
    Plays all movies in a collection
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 2:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    curs.execute("SELECT cm.movie_id FROM collections c INNER JOIN collectionmovie cm on c.collection_id = cm.collection_id WHERE c.name = %s;", (user_input[1], ))
    movie_ids = curs.fetchall()
    if movie_ids is not None:
        for i in movie_ids:
            curs.execute("INSERT INTO usermovie(rating, flag, movie_id, user_id) VALUES (%s, %s, %s, %s);", (None, True, i, user_Id))  # get movie_id from user_input[1]
        print("All movies in collection " + user_input[1] + " has been marked as watched")
        conn.commit()
        return True
    if movie_ids is None:
        print("Collection did not exist")
        return False
    else:
        print("collection not marked as watched")
        return False


def find_friend(user_input, user_Id, conn):
    """
    Finds a list of users with emails in the format user_input[1]?
    :param user_input: The full user command.
    usage: !findfriend [email]
    :param user_Id: the user's id
    :param conn: the connection to the database
    :return: True if friends have been found, False otherwise
    """
    curs = conn.cursor()
    if (user_Id < 0):
        print("You must be logged in to do that. Command aborted.\n")
        return False
    if len(user_input) != 2:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs.execute("SELECT * FROM users WHERE email LIKE %s;", (user_input[1] + "%"))
    fetch = curs.fetchall()
    if len(fetch) == 0:  # no users found
        print("user not found with email " + user_input[1] + "\n")
        return False
    else:  # at least 1 user found
        if len(fetch) == 1:  # 1 user
            print("1 user found.\n")
        if len(fetch) > 1:  # more than 1 user
            print("" + str(len(fetch)) + " users found.\n")
        if len(fetch) < 0:  # bad
            print("Something has gone horribly wrong.\n")
            return False
        fetch.sort()  # alphabetize
        for email in fetch:  # print a list of all users
            print(email[0] + "\n")
        return True


def follow(user_input, user_Id, conn):
    """
    Follow a user
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 2:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    sql = "SELECT * FROM following WHERE follower_UID = %s AND followee_UID = (SELECT user_id FROM users WHERE email = %s);"
    # curs.execute(sql, (user_Id, user_input[1]))
    curs.execute(
        "SELECT * FROM following WHERE follower_UID = %s AND followee_UID = (SELECT user_id FROM users WHERE email = %s);",
        (user_Id, user_input[1]))
    if curs.fetchone() is None:  # if the following combination does not exist
        sql = "INSERT INTO following (follower_UID, followee_UID) VALUES (%s, (SELECT user_id FROM users WHERE email = %s));"
        curs.execute(sql, (user_Id, user_input[1]))
        conn.commit()
        return True
    else:  # if the following combination does exist
        print("you are already following this user")
        return False


def unfollow(user_input, user_Id, conn):
    """
    Unfollows a user
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 2:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    sql = "DELETE FROM following WHERE follower_UID = %s AND followee_uid = (SELECT user_id FROM users WHERE email = %s);"
    curs.execute(sql, (user_Id, user_input[1]))  # deletes if they are following, does nothing if they are not
    conn.commit()
    return True


def display_user_profile(user_input, user_Id,
                         conn):  # displays number of collections, number of followers, number of following, top 10 movies (by rating would be easiest) for a user
    """
    Displays data for a user
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 1:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    sql_get_col = "SELECT collection_id FROM collectionuser WHERE user_id = %s;"
    sql_get_followers = "SELECT follower_uid FROM following WHERE followee_uid = %s;"
    sql_get_following = "SELECT followee_uid FROM following WHERE follower_uid = %s;"
    sql_get_movies = "SELECT m.title FROM usermovie mu INNER JOIN movies m ON mu.movie_id = m.movie_id WHERE user_id = %s ORDER BY user_rating DESC;"

    curs.execute(sql_get_col, (user_Id,))  # iterable of all collections
    cols = curs.fetchall()
    curs.execute(sql_get_followers, (user_Id,))  # iterable of all followeruids
    followers = curs.fetchall()
    curs.execute(sql_get_following, (user_Id,))  # iterable of all followeeuids
    following = curs.fetchall()
    curs.execute(sql_get_movies, (user_Id,))  # iterable of all movieids, sorted by rating
    movies = curs.fetchall()

    print("Your user stats:")
    print("Collections: " + str(len(cols)))
    print("Followers: " + str(len(followers)))
    print("Followees: " + str(len(following)))
    print("Movies watched: " + str(len(movies)))
    print("Top movies by user rating:")
    for x in range(min(10, len(movies))):
        print(*movies[x])
    return True


def top_20_last_90(user_input, user_Id, conn):
    """
    Displays top 20 movies of the past 90 days
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 1:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    three_months = int(time.time()) - (60 * 60 * 24 * 90)  # 90 days ago
    ninety_days = datetime.utcfromtimestamp(three_months).strftime('%Y-%m-%d')
    print("Showing most viewed movies past " + ninety_days)
    curs = conn.cursor()
    curs.execute("SELECT COUNT(u.movie_id) as occurances, m.title FROM usermovie u INNER JOIN movies m on m.movie_id = u.movie_id "
                 "WHERE date_gt(u.watch_date, %s) GROUP BY u.movie_id, title ORDER BY occurances DESC limit 20", (ninety_days, ))
    rows = curs.fetchall()
    for row in rows:
        print("Movie Title: " + row[1] + " has been watched " + str(row[0]) + "time(s)")
    return True

def top_20_friends(user_input, user_Id, conn):
    """
    Displays top 20 movies from friends
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 1:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    # complex sql statement
    # this sql statement inner joins following on itself to find users that the current user follows that also follow
    # them, and returns the top 20 watched movies by those users.
    sql = "select m.title from following f1 inner join following f2 on f1.followee_uid = f2.follower_uid INNER JOIN " \
          "usermovie mu on mu.user_id = f1.followee_uid INNER JOIN movies m ON m.movie_id = mu.movie_id WHERE " \
          "f1.follower_uid = f2.followee_uid AND f1.followee_uid = %s AND mu.flag IS NOT NULL GROUP BY m.movie_id " \
          "ORDER BY count(*) DESC LIMIT 20"
    curs.execute(sql, (user_Id,))
    friend_movies = curs.fetchall()
    print("Your friend's movies:")
    for x in friend_movies:
        print(*x)
    return True


def last_5_releases(user_input, user_Id, conn):
    """
    Displays the latest 5 releases
    :param user_input: The full command the user inputs
    :param user_id: the current user's user_Id
    :param conn: the connection to the database
    :return: True on success, False on failure
    """
    if len(user_input) != 1:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    sql = "SELECT title FROM public.movies t ORDER BY release_date DESC limit 5;"
    curs.execute(sql)
    rows = curs.fetchall()
    for row in rows:
        print("Movie Title: " + row[0])
    return True


def recommended_movies(user_input, user_Id, conn):
    """
    Generates recommended movies based on movies previously watched

    Looks at the user's top 3 most watched genres and randomly pulls 10 unwatched movies from those genres
    :param user_input: The full command the user inputs
    usage: !createcollection [collection_name]
    :param user_Id: the current user's user_Id
    :param conn: the connection to the database
    :return: True
    """
    if len(user_input) != 1:
        print("Illegal use of this command, incorrect number of statements. Command aborted.\n")
        return False
    curs = conn.cursor()
    # gets genre_id's of watched movies in descending popularity
    sql_genres_pop = "SELECT genre_id FROM genremovies gm INNER JOIN usermovie mu ON gm.movie_id = mu.movie_id WHERE " \
                     "mu.user_id = %s GROUP BY gm.genre_id ORDER BY count(*) DESC; "
    curs.execute(sql_genres_pop, (user_Id,))
    genres_pop = curs.fetchall()
    # complex sql statement
    # this sql statement selects titles of movies that the current user has not watched,
    # that have genres that the user likes
    sql_unw_movies = "SELECT m.title FROM movies m LEFT OUTER JOIN usermovie mu ON m.movie_id = mu.movie_id INNER " \
                     "JOIN genremovies gm ON m.movie_id = gm.movie_id WHERE (mu.user_id != %s OR mu.user_id IS NULL) " \
                     "AND (gm.genre_id = %s OR gm.genre_id = %s OR gm.genre_id = %s) GROUP BY m.movie_id ORDER BY " \
                     "RANDOM() LIMIT 10;"
    curs.execute(sql_unw_movies, (user_Id, genres_pop[0], genres_pop[1], genres_pop[2]))
    movies_rec = curs.fetchall()
    print("Your recommendations based on previously watched movies:")
    for x in range(10):
        print(*movies_rec[x])
    return True


def console(conn):
    exit = False
    user_Id = -1
    while not exit:
        user_input = input("Please enter a command. Type !help for a list of all available commands. \n").split(" ")
        if user_input[0] == "!help":
            help()
        elif user_Id == -1:
            if user_input[0] == "!login":
                user_Id = login(conn)
            elif user_input[0] == "!createacc":
                create_acc(user_input, conn)
            elif user_input[0] == "!exit":
                exit = True
        else:
            # ("userid: ", user_Id)
            exit = process_commands(user_input, user_Id, conn)
    print("Quitting program...")
