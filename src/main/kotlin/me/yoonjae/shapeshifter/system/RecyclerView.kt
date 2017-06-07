package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize

class RecyclerView : SwiftFile("RecyclerView.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("RecyclerView") {
            superType("FrameLayout")

            variable("adapter", Type("Adapter", true), "nil")

            initializer {
                public()
                parameter("theme", Type("Theme"), "AppTheme()")
                parameter("id", Type("String", true), "nil")
                parameter("layoutParams", Type("LayoutParams"))
                parameter("padding", Type("UIEdgeInsets")) {
                    initializerExpression("UIEdgeInsets")
                }
                parameter("minWidth", Type("CGFloat", true), "nil")
                parameter("minHeight", Type("CGFloat", true), "nil")
                parameter("alpha", Type("CGFloat"), "1.0")
                parameter("background", Type("UIColor", true), "nil")
                parameter("adapter", Type("Adapter", true), "nil")
                parameter("config", Type("(UITableView) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                    argument("children") {
                        arrayLiteralExpression {
                            initializerExpression("View<UITableView>") {
                                argument("theme", "theme")
                                argument("layoutParams") {
                                    initializerExpression("LayoutParams") {
                                        argument("width", "MATCH_PARENT")
                                        argument("height", "MATCH_PARENT")
                                    }
                                }
                                argument("alpha", "alpha")
                                argument("background", "background")
                            }
                        }
                    }
                }
                assignmentExpression("self.adapter", "adapter")
                assignmentExpression("(self.children[0] as! View<UITableView>).config") {
                    closureExpression {
                        closureParameter("view")
                        assignmentExpression("view.separatorStyle", value = ".none")
                        assignmentExpression("view.dataSource", value = "self.adapter")
                        assignmentExpression("view.delegate", value = "self.adapter")
                        functionCallExpression("view.reloadData")
                        functionCallExpression("config?") {
                            argument(value = "view")
                        }
                    }
                }
            }

            function("measurement", Type("LayoutMeasurement")) {
                override()
                public()
                parameter("maxSize", Type("CGSize"), label = "within")

                variable("size") {
                    functionCallExpression("maxSize.decreasedToSize") {
                        argument {
                            functionCallExpression("CGSize") {
                                argument("width",
                                        "layoutParams.width == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.width == WRAP_CONTENT ? " +
                                                "padding.left + padding.right : " +
                                                "layoutParams.margin.left + layoutParams.margin.right + " +
                                                "layoutParams.width)")
                                argument("height",
                                        "layoutParams.height == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.height == WRAP_CONTENT ? " +
                                                "padding.top + padding.bottom : " +
                                                "layoutParams.margin.top + layoutParams.margin.bottom + " +
                                                "layoutParams.height)")
                            }
                        }
                    }
                }
                ifStatement("layoutParams.height == WRAP_CONTENT") {
                    ifStatement("let adapter = adapter") {
                        constant("tableView", value = "(self.children[0] as! View<UITableView>)" +
                                ".view ?? UITableView()")
                        forInStatement("section", "0..<adapter.numberOfSections(in: tableView)") {
                            forInStatement("row", "0..<adapter.tableView(tableView, " +
                                    "numberOfRowsInSection:section)") {
                                assignmentExpression("size.height",
                                        "size.height + adapter.tableView(tableView, " +
                                                "heightForRowAt: IndexPath(row: row," +
                                                "section: section))")
                            }
                        }
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutMeasurement") {
                        argument("layout", "self")
                        argument("size") {
                            increasedToMinSize()
                        }
                        argument("maxSize", "maxSize")
                        argument("sublayouts", "[children[0].measurement(within: size)]")
                    }
                }
            }

            clazz("Adapter") {
                public()
                superType("NSObject")
                superType("UITableViewDelegate")
                superType("UITableViewDataSource")

                variable("items", value = "[Any]()")

                initializer {
                    override()
                    public()
                }

                function("numberOfSections", Type("Int")) {
                    parameter("tableView", Type("UITableView"), label = "in")

                    returnStatement("1")
                }

                function("tableView", Type("Int")) {
                    parameter("tableView", Type("UITableView"), label = "_")
                    parameter("section", Type("Int"), label = "numberOfRowsInSection")

                    returnStatement("items.count")
                }

                function("tableView", Type("UITableViewCell")) {
                    parameter("tableView", Type("UITableView"), label = "_")
                    parameter("indexPath", Type("IndexPath"), label = "cellForRowAt")

                    returnStatement("UITableViewCell()")
                }

                function("tableView", Type("CGFloat")) {
                    parameter("tableView", Type("UITableView"), label = "_")
                    parameter("indexPath", Type("IndexPath"), label = "heightForRowAt")

                    returnStatement("0")
                }
            }
        }
    }
}