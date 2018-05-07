import {Item} from "./item";

export const ITEMS: Item[] = [
  {
    itemId: 1,
    name: "Coffee",
    description: "Description",
    ownerId: 2,
    bookerId: 12,
    priority: "HIGH",
    imageFilepath: "someimage",
    link: "",
    dueDate: "2018-06-05",
    likes: 3,
    tags: ["ruha"]
    // tags: [
    //   {name: "runnnnn"},
    //   {name: "runnnnnnnnnnn"},
    //   {name: "ru"}
    // ]
  },
  {
    itemId: 4,
    name: "Coffee2",
    description: "Description2",
    ownerId: 1,
    bookerId: 12,
    priority: "MEDIUM",
    imageFilepath: "someimage",
    link: "",
    dueDate: "2018-05-25",
    likes: 2,
    tags: ["runnnn_77874785n"]
    // tags: [
    //   {name: "runnnnn"},
    //   {name: "hfgh"},
    //   {name: "runnnnn"},
    //   {name: "runnnn_77874785n"}
    // ]
  }
];
