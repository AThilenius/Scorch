class CreateAssignmentDescriptions < ActiveRecord::Migration
  def change
    create_table :assignment_descriptions do |t|
      t.string    :name
      t.text      :markdown
      t.text      :brief_markdown
      t.string    :jarPath
      t.datetime  :dueDate
      t.datetime  :open_date
      t.timestamps
    end
  end
end
