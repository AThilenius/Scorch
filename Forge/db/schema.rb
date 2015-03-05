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

ActiveRecord::Schema.define(version: 20150207212218) do

  create_table "anvil_runs", force: true do |t|
    t.integer  "user_id"
    t.string   "calling_ip"
    t.integer  "awarded_points"
    t.integer  "blaze_call_count"
    t.binary   "calling_code"
    t.binary   "api_calls"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "anvil_runs", ["user_id"], name: "index_anvil_runs_on_user_id"

# Could not dump table "assignment_descriptions" because of following NoMethodError
#   undefined method `[]' for nil:NilClass

  create_table "level_descriptions", force: true do |t|
    t.integer  "assignment_description_id"
    t.string   "name"
    t.integer  "levelNumber"
    t.integer  "points"
    t.string   "markdown"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "extra_credit"
  end

# Could not dump table "minecraft_accounts" because of following NoMethodError
#   undefined method `[]' for nil:NilClass

  create_table "user_assignments", force: true do |t|
    t.integer  "user_id"
    t.integer  "assignment_description_id"
    t.string   "authToken"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "user_assignments", ["assignment_description_id"], name: "index_user_assignments_on_assignment_description_id"
  add_index "user_assignments", ["authToken"], name: "authToken_index"
  add_index "user_assignments", ["user_id"], name: "index_user_assignments_on_user_id"

  create_table "user_levels", force: true do |t|
    t.integer  "user_assignment_id"
    t.integer  "level_description_id"
    t.integer  "points",               default: 0
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "extra_credit",         default: 0, null: false
  end

  add_index "user_levels", ["level_description_id"], name: "index_user_levels_on_level_description_id"
  add_index "user_levels", ["user_assignment_id"], name: "index_user_levels_on_user_assignment_id"

# Could not dump table "users" because of following NoMethodError
#   undefined method `[]' for nil:NilClass

end
