import sqlite3
import re
from flask import Flask, request, session, g, \
    redirect, url_for, render_template
from contextlib import closing

# configuration
DATABASE = 'database.db'
DEBUG = True
SECRET_KEY = 'RGINQ4T348TB8GQ'
USERNAME = 'admin'
PASSWORD = 'admin'

app = Flask(__name__)
app.config.from_object(__name__)

####### Database

def connect_db():
    return sqlite3.connect(app.config['DATABASE'])

def init_db():
    with closing(connect_db()) as db:
        with app.open_resource('schema.sql', mode='r') as f:
            db.cursor().executescript(f.read())
        db.commit()

@app.before_request
def before_request():
    g.db = connect_db()

@app.teardown_request
def teardown_request(exception):
    db = getattr(g, 'db', None)
    if db is not None:
        db.close()

#######

@app.route("/", methods=['GET','POST'])
def index():
    if request.method == 'POST':
        results = g.db.execute('SELECT * FROM notes WHERE TITLE LIKE ? OR DESCRIPTION LIKE ? ORDER BY TITLE', \
                              ['%'+request.form['search']+'%', '%'+request.form['search']+'%'])
    else:
        results = g.db.execute('SELECT * FROM notes ORDER BY TITLE ')
    notes=[]
    for row in results:
        new_row=[]
        for column in range(len(row)):
            if column == 2:
                new_row.append(markup(row[column]))
            else:
                new_row.append(row[column])
        notes.append(new_row)
    return render_template('index.html', notes=notes)

@app.route("/login", methods=['POST'])
def login():
    if request.form['username'] == app.config['USERNAME']:
        if request.form['password'] == app.config['PASSWORD']:
            session['logged_in'] = True
    return redirect(url_for('index'))

@app.route("/logout")
def logout():
    session.pop('logged_in', None)
    session.pop('username', None)
    return redirect(url_for('index'))

@app.route('/edit/<index>', methods=['GET','POST'])
def edit(index):
    if request.method == 'POST':
        g.db.execute("UPDATE notes SET TITLE=?,DESCRIPTION=?,TSTAMP=CURRENT_TIMESTAMP WHERE ID=?", [request.form['title'],request.form['description'],index])
        g.db.commit()
        return redirect(url_for('index'))
    else:
        results = g.db.execute("SELECT TITLE, DESCRIPTION FROM notes WHERE ID=?", [index])
        return render_template("edit.html", notes=results)

@app.route("/view/<index>")
def view(index):
    results = g.db.execute("SELECT * FROM notes WHERE ID=?", [index])
    notes=[]
    for row in results:
        new_row=[]
        for column in range(len(row)):
            if column == 2:
                new_row.append(markup(row[column]))
            else:
                new_row.append(row[column])
        notes.append(new_row)
    return render_template("view.html", notes=notes)

@app.route("/add", methods=['POST'])
def add():
    title, description = request.form['title'], request.form['description']
    g.db.execute("INSERT INTO notes (TITLE, DESCRIPTION) VALUES (?, ?)", [title, description])
    g.db.commit()
    return redirect(url_for('index'))

@app.route("/delete/<index>")
def delete(index):
    g.db.execute("DELETE FROM notes WHERE ID=?", [index])
    g.db.commit()
    return redirect(url_for('index'))

def markup(content):
    new_content = []
    is_fixed = False
    is_list = False
    for line in content.splitlines():
        if line.startswith("*#BEGIN_FIXED"):
            is_fixed = True
            line = "<div class='fixed'>\n<pre>"+line[13:]
        elif line.endswith("*#END_FIXED"):
            is_fixed = False
            line = line[:len(line)-11]+"</pre></div>"
        elif is_fixed:
            new_content.append(line)
            continue

        if line.startswith("-"):
            if not is_list:
                new_content.append("<ul>")
                is_list = True
            line = "<li>"+line[1:]+"</li>"
        elif is_list:
            is_list = False
            new_content.append("</ul>")

        if line.startswith("**"):
            line = "<h3>"+line[2:]+"</h3>"
        elif line.startswith("*"):
            line = "<h2>"+line[1:]+"</h2>"

        line = re.sub(r'\[\[(.*)\]\]',r'<a href="\1">\1</a>',line)

        new_content.append(line)
    return "\n".join(new_content)



if __name__ == "__main__":
    init_db()
    app.run()

#TODO: Search symbol