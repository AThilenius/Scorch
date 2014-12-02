# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20141129232453) do

  create_table "assignment_descriptions", force: true do |t|
    t.string   "name"
    t.string   "markdown"
    t.string   "jarPath"
    t.datetime "dueDate"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "level_descriptions", force: true do |t|
    t.integer  "assignment_description_id"
    t.string   "name"
    t.integer  "levelNumber"
    t.integer  "points"
    t.string   "markdown"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "level_descriptions", ["assignment_description_id"], name: "index_level_descriptions_on_assignment_description_id"

  create_table "minecraft_accounts", force: true do |t|
    t.string   "username"
    t.string   "password"
    t.string   "state"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "user_assignments", force: true do |t|
    t.integer  "user_id"
    t.integer  "assignment_description_id"
    t.string   "authToken"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "user_assignments", ["assignment_description_id"], name: "index_user_assignments_on_assignment_description_id"
  add_index "user_assignments", ["user_id"], name: "index_user_assignments_on_user_id"

  create_table "user_levels", force: true do |t|
    t.integer  "user_assignment_id"
    t.integer  "level_description_id"
    t.integer  "points",               default: 0
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "user_levels", ["level_description_id"], name: "index_user_levels_on_level_description_id"
  add_index "user_levels", ["user_assignment_id"], name: "index_user_levels_on_user_assignment_id"

# Could not dump table "users" because of following NoMethodError
#   undefined method `[]' for nil:NilClass

end
