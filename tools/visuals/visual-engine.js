/* AAS2 build-time extraction of the user-owned Scholars Visual Learning Engine v1.6.0. Source legacy blob: 7f0a4a93665468429ba33560bf844fc6447f0a72. Not bundled as an Android runtime dependency. */
/* Offline visual learning engine: semantic SVG illustrations for every bank question. */
(function(root){
"use strict";
var NUMBERS={zero:0,one:1,two:2,three:3,four:4,five:5,six:6,seven:7,eight:8,nine:9,ten:10,eleven:11,twelve:12,thirteen:13,fourteen:14,fifteen:15,sixteen:16,seventeen:17,eighteen:18,nineteen:19,twenty:20};
var TERMS={
 apple:["apple","apples"],banana:["banana","bananas"],orange:["orange","oranges"],grape:["grape","grapes"],lemon:["lemon","lemons"],turtle:["turtle","turtles"],potato:["potato","potatoes"],cotton:["cotton","cotton fibre","cotton fibres","cotton fiber","cotton fibers"],bean:["bean","beans"],herbivore:["herbivore","herbivores"],circulation:["circulation","blood circulation"],hygiene:["hygiene"],solid:["solid","solids"],simple_machine:["simple machine","simple machines"],mammal:["mammal","mammals"],desert:["desert","deserts"],material:["material","materials"],magnet:["magnet","magnets"],animal_needs:["animal needs"],body_parts:["body parts"],safety:["safety"],floating:["floating"],melting:["melting"],bean_growth:["bean growth"],potato_plant:["potato plant"],pentagon:["pentagon","pentagons"],hexagon:["hexagon","hexagons"],mango:["mango","mangoes"],donkey:["donkey","donkeys"],frog:["frog","frogs","froglet","froglets"],spider:["spider","spiders"],octopus:["octopus","octopuses"],tadpole:["tadpole","tadpoles"],caterpillar:["caterpillar","caterpillars"],pupa:["pupa","pupae","chrysalis","chrysalises"],polar_bear:["polar bear","polar bears"],tooth:["tooth","teeth"],doctor:["doctor","doctors"],farm:["farm","farms","farmer","farmers"],baker:["baker","bakers"],ramp:["ramp","ramps"],pulley:["pulley","pulleys"],lever:["lever","levers"],rib_cage:["rib cage","rib cages","backbone"],plastic:["plastic"],scale:["scale","scales","weighing scales"],cake:["cake","cakes"],tap:["tap","taps","faucet","faucets"],bin:["bin","recycling bin","recycle bin"],water_vapour:["water vapour","water vapor"],food_chain:["food chain","food chains"],life_cycle:["life cycle","life cycles"],skeleton:["skeleton","skeletons","vertebrate","vertebrates","invertebrate","invertebrates"],gravity:["gravity"],photosynthesis:["photosynthesis"],day_night:["day and night"],balanced_diet:["balanced diet"],recycling:["recycling"],predator:["predator","predators"],prey:["prey"],insect:["insect","insects"],habitat:["habitat","habitats"],evaporation:["evaporation"],water_cycle:["water cycle"],mirror:["mirror","mirrors"],coat:["coat","coats"],cold:["cold","chilly"],
 star:["star","stars"],cat:["cat","cats","kitten","kittens"],dog:["dog","dogs","puppy","puppies"],fox:["fox","foxes"],bird:["bird","birds","chicken","chickens","hen","hens","duck","ducks","owl","owls"],
 fish:["fish","fishes"],bee:["bee","bees"],butterfly:["butterfly","butterflies"],pig:["pig","pigs"],cow:["cow","cows"],goat:["goat","goats"],camel:["camel","camels"],rabbit:["rabbit","rabbits"],elephant:["elephant","elephants"],
 sun:["sun","sunlight","sunny","daylight","light"],moon:["moon"],cloud:["cloud","clouds"],rain:["rain","rainy"],wind:["wind","windy","air"],rainbow:["rainbow","rainbows"],
 flower:["flower","flowers","sunflower","sunflowers"],plant:["plant","plants","sprout","sprouts"],tree:["tree","trees"],seed:["seed","seeds"],leaf:["leaf","leaves"],water:["water","watering"],
 pencil:["pencil","pencils"],book:["book","books","storybook","storybook"],car:["car","cars"],bus:["bus","buses"],train:["train","trains"],plane:["plane","planes","airplane","airplanes"],boat:["boat","boats","ship","ships"],
 ball:["ball","balls","football"],house:["house","houses","home"],school:["school","schools","classroom"],teacher:["teacher","teachers"],window:["window","windows"],robot:["robot","robots"],hand:["hand","hands","finger","fingers"],soap:["soap"],germ:["germ","germs"],glass:["glass"],skull:["skull"],brain:["brain"],cork:["cork"],stove:["stove","stoves"],raincoat:["raincoat","raincoats"],plate:["plate","plates"],coin:["coin","coins","money","cents"],rock:["rock","rocks","stone","stones"],toy:["toy","toys","teddy","teddies"],
 egg:["egg","eggs"],bread:["bread"],milk:["milk"],carrot:["carrot","carrots"],tomato:["tomato","tomatoes"],heart:["heart","hearts"],eye:["eye","eyes"],nose:["nose","noses"],leg:["leg","legs"],hat:["hat","hats"],sock:["sock","socks"],shoe:["shoe","shoes","boot","boots"],bed:["bed","beds"],chair:["chair","chairs"],cup:["cup","cups"],umbrella:["umbrella","umbrellas"],shadow:["shadow","shadows"],map:["map","maps"],nest:["nest","nests"],pencil:["pencil","pencils"],block:["block","blocks"],ribbon:["ribbon","ribbons"],rope:["rope","ropes"],shell:["shell","shells"],soil:["soil"],planet:["planet","planets","earth"],rocket:["rocket","rockets","space"],shape:["shape","shapes"],circle:["circle","circles"],square:["square","squares"],triangle:["triangle","triangles"],rectangle:["rectangle","rectangles"],letter:["letter","letters","alphabet"],number:["number","numbers","digit","digits"],clock:["clock","clocks","time"],spoon:["spoon","spoons"],table:["table","tables"],door:["door","doors"],cupboard:["cupboard","cupboards"],pollen:["pollen"],bee:["bee","bees"],flower:["flower","flowers","blossom","blossoms"],waterdrop:["droplet","droplets"],
 ant:["ant","ants"],top:["top","tops"],tin:["tin","tins","can"],bat:["bat","bats"],pen:["pen","pens"],pan:["pan","pans"],mat:["mat","mats"],net:["net","nets"],van:["van","vans"],jug:["jug","jugs"],log:["log","logs"],peg:["peg","pegs"],jam:["jam"],cap:["cap","caps"],smile:["smile","smiles","smiling"],person:["person","people","student","students","child","children","boy","boys","girl","girls","mum","mother","father","family","he","she"],ear:["ear","ears","hearing","hear"],sight:["sight","see","seeing","vision"],smell:["smell","smelling"],taste:["taste","tasting","tongue"],touch:["touch","feeling","feel"],season:["season","seasons","spring","summer","autumn","fall","winter"],ice:["ice","snow","arctic","polar"],magnet:["magnet","magnetic","attracted"],iron:["iron"],wood:["wood","wooden"],paper:["paper"],metal:["metal","metals","conductor","conductors"],lungs:["lung","lungs","breathing","breathe"],continent:["continent","continents","africa","europe","asia"],pond:["pond","ponds"],road:["road","roads"],bicycle:["bicycle","bicycles","bike","bikes"],phone:["phone","phones"],dot:["dot","dots","counter","counters"],ruler:["ruler","rulers","centimetre","centimetres","centimeter","centimeters","measure","measuring"],sticker:["sticker","stickers"],small:["small","tiny","little"],big:["big","bigger","huge","large"],loud:["loud","louder","noise","sound"],group:["group","groups"],color:["colour","color","colours","colors"],red:["red"],blue:["blue"],green:["green"],yellow:["yellow"],purple:["purple"],white:["white"],black:["black"],brown:["brown"],grey:["grey","gray"],bear:["bear","bears"],elephant:["elephant","elephants"],kite:["kite","kites"],whale:["whale","whales"],zebra:["zebra","zebras"],grape:["grape","grapes"],leaf:["leaf","leaves"],food:["food","foods","nutrition","diet","meal","meals"],spring:["spring"],summer:["summer"],autumn:["autumn","fall"],winter:["winter"],xylophone:["xylophone"],yoyo:["yo-yo","yoyo"],canvas:["canvas","drawing"],
fraction:["fraction","equal parts","shaded"],piaster:["piaster","piasters","piastre","piastres"],pound:["pound","pounds"],temperature:["temperature","hot","cold"],thermometer:["thermometer"],time:["time","hour","hours","o'clock","duration"],weight:["weight","heavier","heaviest"],kilogram:["kilogram","kilograms","kg"],gram:["gram","grams","g"],scale:["scale","balance","weighing scale"]
};
Object.assign(TERMS, {
 plant: TERMS.plant.concat(["root","roots","stem","stems","seedling","seedlings","germination","germinate"]),
 tree: TERMS.tree.concat(["date palm","date palms","wooden","timber"]),
 seed: TERMS.seed.concat(["sesame","sorghum","lentils","grain","grains","peas"]),
 flower: TERMS.flower.concat(["broccoli","flower buds","blossom"]),
 leaf: TERMS.leaf.concat(["lettuce","spinach"]),
 water: TERMS.water.concat(["damp paper","moist paper"])
});
var EMOJI={"🍎":"apple","⭐":"star","🌟":"star","🐶":"dog","🐱":"cat","🐟":"fish","🐦":"bird","🌱":"plant","🌸":"flower","🌳":"tree","☀":"sun","☀️":"sun","🌙":"moon","☁":"cloud","☁️":"cloud","🌧":"rain","🌧️":"rain","💧":"water","✏":"pencil","✏️":"pencil","📖":"book","🚗":"car","🚌":"bus","⚽":"ball","🍌":"banana","🍊":"orange","🐷":"pig","🐝":"bee","🦋":"butterfly","🪨":"rock","🧸":"toy","🥚":"egg","🪙":"coin","✋":"hand","🖐":"hand","🖐️":"hand","🪴":"plant","🍅":"tomato","🥕":"carrot","🏠":"house","🏫":"school","🚂":"train","✈":"plane","✈️":"plane","🪺":"nest","🐄":"cow","🐰":"rabbit","🐘":"elephant","🪑":"chair","🛏":"bed","🛏️":"bed","🧢":"hat","🧦":"sock","👟":"shoe","☂":"umbrella","☂️":"umbrella","🗺":"map","🗺️":"map","🐚":"shell","🧱":"block","🟢":"green","🔴":"red","🔵":"blue","🟡":"yellow","🟣":"purple","⚫":"black","⚪":"white","🟤":"brown","🩶":"grey","📱":"phone","📚":"book","🚲":"bicycle","👂":"ear","👁":"eye","👁️":"eye","🧲":"magnet","❄":"ice","❄️":"ice","🌍":"continent","🌎":"continent","🌏":"continent","🧍":"person","👨‍👩‍👧":"person"};
function esc(s){return String(s===undefined||s===null?"":s).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#39;");}
function norm(s){return String(s||"").toLowerCase().replace(/[^a-z0-9]+/g," ").replace(/\s+/g," ").trim();}
function hasWord(text,word){return (" "+norm(text)+" ").indexOf(" "+word+" ")>=0;}
function findKinds(text){
 var out=[],seen={};var n=norm(text);
 Object.keys(TERMS).forEach(function(k){
  var found=TERMS[k].some(function(term){return (" "+n+" ").indexOf(" "+term+" ")>=0;});
  if(found&&!seen[k]){seen[k]=1;out.push(k);}
 });
 return out;
}
function kindOf(text){
 var s=String(text||"");
 for(var e in EMOJI)if(s.indexOf(e)>=0)return EMOJI[e];
 var exact=norm(s);
 if(Object.prototype.hasOwnProperty.call(TERMS,exact))return exact;
 return findKinds(s)[0]||"generic";
}
function numOf(x){
 if(typeof x==="number"&&isFinite(x))return x;
 var s=norm(x);
 if(/^\d+$/.test(s))return Number(s);
 var words=s.split(" ");
 for(var i=0;i<words.length;i++)if(Object.prototype.hasOwnProperty.call(NUMBERS,words[i]))return NUMBERS[words[i]];
 return null;
}
function numbersIn(text){
 var s=String(text||""),out=[],m,re=/\b\d{1,3}\b|\b(?:zero|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve|thirteen|fourteen|fifteen|sixteen|seventeen|eighteen|nineteen|twenty)\b/gi;
 while((m=re.exec(s))!==null){var n=numOf(m[0]);if(n!==null)out.push({value:n,raw:m[0],index:m.index});}
 return out;
}
function body(kind){
 switch(kind){
 case "apple":return '<path d="M50 28C24 15 12 35 21 61c6 19 17 25 29 17 12 8 23 2 29-17 9-26-3-46-29-33Z" fill="#ef4444" stroke="#a92735" stroke-width="3"/><path d="M50 29c-3-12 2-18 10-22" fill="none" stroke="#79502e" stroke-width="5" stroke-linecap="round"/><path d="M55 14c13-10 25-3 23 8-10 6-20 3-23-8Z" fill="#35a853" stroke="#20763a" stroke-width="2"/><path d="M31 42c-4 8-3 16 1 20" fill="none" stroke="#ffb1a5" stroke-width="4" stroke-linecap="round"/>';
 case "banana":return '<path d="M18 22c8 32 29 51 57 40 8-3 13-9 15-17-9 6-17 7-25 3-18-8-29-25-34-43-6 4-10 10-13 17Z" fill="#ffd43b" stroke="#bd8619" stroke-width="3"/><path d="M18 22l-5-5m73 28 7 1" stroke="#805b27" stroke-width="5" stroke-linecap="round"/>';
 case "orange":return '<circle cx="50" cy="49" r="31" fill="#ff9b32" stroke="#d66b16" stroke-width="3"/><path d="M48 18c0-9 7-12 15-10-1 9-7 14-15 10Z" fill="#39984b" stroke="#26733a" stroke-width="2"/><path d="M50 20v-7" stroke="#79502e" stroke-width="4"/><circle cx="39" cy="39" r="3" fill="#ffc16c"/><circle cx="61" cy="55" r="3" fill="#ffc16c"/>';
 case "mango":return '<path d="M48 16C22 17 10 39 22 65c10 23 39 24 55 3 18-25 4-52-29-52Z" fill="#f4ad32" stroke="#b66b1d" stroke-width="3"/><path d="M58 13c15-9 27-2 25 10-11 6-22 3-25-10Z" fill="#3c9b55" stroke="#26723d" stroke-width="2"/>';
 case "donkey":return '<ellipse cx="50" cy="58" rx="30" ry="22" fill="#9ca3af" stroke="#59616b" stroke-width="3"/><path d="M63 48q8-19 5-31l9-12 5 28q13 5 9 21-7 16-25 9Z" fill="#aeb5bf" stroke="#59616b" stroke-width="3"/><path d="M68 19 66 2 58 0 58 22M79 18 84 2 91 4 86 27" fill="#858d98" stroke="#59616b" stroke-width="3"/><circle cx="78" cy="39" r="3" fill="#263b4d"/><path d="M29 73v15m15-12v12m22-14v14m12-18v18" stroke="#59616b" stroke-width="5" stroke-linecap="round"/>';
 case "frog":return '<ellipse cx="50" cy="57" rx="31" ry="24" fill="#5dbb68" stroke="#287d43" stroke-width="3"/><circle cx="31" cy="31" r="13" fill="#75ce79" stroke="#287d43" stroke-width="3"/><circle cx="69" cy="31" r="13" fill="#75ce79" stroke="#287d43" stroke-width="3"/><circle cx="31" cy="31" r="5" fill="#263b4d"/><circle cx="69" cy="31" r="5" fill="#263b4d"/><path d="M33 61q17 15 34 0M27 75l-14 10m60-10 14 10" fill="none" stroke="#287d43" stroke-width="4"/>';
 case "spider":return '<circle cx="50" cy="52" r="17" fill="#394b5c"/><circle cx="50" cy="29" r="12" fill="#536879"/><path d="M36 42 16 24 6 30m8 24L4 53m32 12L15 82 7 77m57-35 20-18 10 6m-9 17 12-1m-33 21 21 17 8-5" fill="none" stroke="#394b5c" stroke-width="5" stroke-linecap="round"/>';
 case "octopus":return '<path d="M25 53c-12 17-12 31-2 31s9-12 16-15c5 19 17 24 21 12 2-7-3-13-4-18 14 19 28 18 29 7 1-8-8-13-17-18 20 8 31-1 25-10-5-7-17-5-27-2 13-14 8-31-3-31-8 0-12 10-13 20-7-20-24-23-29-12-4 8 3 17 12 23-19-7-31 2-25 12 4 6 14 5 27 1Z" fill="#a879d6" stroke="#65439b" stroke-width="3"/><circle cx="43" cy="43" r="3" fill="#263b4d"/><circle cx="59" cy="43" r="3" fill="#263b4d"/>';
 case "tadpole":return '<circle cx="35" cy="48" r="18" fill="#4caa62" stroke="#26723d" stroke-width="3"/><path d="M51 49q24-18 43 0-18 4-32 13" fill="#75c77b" stroke="#26723d" stroke-width="3"/>';
 case "caterpillar":return '<circle cx="23" cy="56" r="16" fill="#8acb5a" stroke="#4b8b35" stroke-width="3"/><circle cx="44" cy="56" r="16" fill="#78bd4f" stroke="#4b8b35" stroke-width="3"/><circle cx="65" cy="56" r="16" fill="#8acb5a" stroke="#4b8b35" stroke-width="3"/><circle cx="82" cy="48" r="14" fill="#62ad47" stroke="#4b8b35" stroke-width="3"/>';
 case "pupa":return '<path d="M48 15q-19 13-13 40 4 23 17 27 16-13 16-34 0-23-20-33Z" fill="#a6b96b" stroke="#647c43" stroke-width="3"/><path d="M19 15q31 9 60 0" stroke="#7d5933" stroke-width="4" fill="none"/>';
 case "tooth":return '<path d="M27 18q-14 4-12 23 3 14 11 19 7 4 11 22 3 11 9 8 6-3 7-17 2-13 7-13 6 0 8 13 2 14 8 16 7 2 9-11 2-16 8-25 9-13 0-29-9-15-24-8-14 7-25 1-10-5-17 1Z" fill="#fffdf1" stroke="#9aa8b5" stroke-width="3"/>';
 case "doctor":return '<circle cx="50" cy="22" r="15" fill="#d99b70" stroke="#885b40" stroke-width="3"/><path d="M28 43q22-14 44 0l9 42H19Z" fill="#f7fbff" stroke="#7895ad" stroke-width="3"/><path d="M42 47v20q8 12 16 0V47" stroke="#3e8fbb" stroke-width="4" fill="none"/>';
 case "farm":return '<path d="M10 48 50 17 90 48v36H10Z" fill="#e6a34e" stroke="#9a642e" stroke-width="3"/><path d="M34 84V55h32v29" fill="#b85d45" stroke="#7d3d31" stroke-width="3"/><path d="M5 91q22-15 45 0t45 0" stroke="#5e9d55" stroke-width="5" fill="none"/>';
 case "baker":return '<circle cx="50" cy="25" r="14" fill="#d99b70" stroke="#885b40" stroke-width="3"/><path d="M34 14q16-21 32 0" fill="#fff" stroke="#a6b3bf" stroke-width="3"/><path d="M25 45q25-16 50 0l6 40H19Z" fill="#fff" stroke="#a6b3bf" stroke-width="3"/><path d="M38 68q-2-17 12-17t12 17q-12 10-24 0Z" fill="#dca45c" stroke="#9c642f" stroke-width="3"/>';
 case "ramp":return '<path d="M10 83 83 32 91 83Z" fill="#d7e4ef" stroke="#6e8da7" stroke-width="3"/><rect x="43" y="43" width="19" height="12" rx="3" fill="#e65b4e" stroke="#9b3e36" stroke-width="2" transform="rotate(-34 52 49)"/>';
 case "pulley":return '<circle cx="50" cy="36" r="21" fill="#dce7ef" stroke="#607b91" stroke-width="5"/><circle cx="50" cy="36" r="6" fill="#607b91"/><path d="M29 36v42q0 12 13 12h16q13 0 13-12V36" fill="none" stroke="#b67a3d" stroke-width="4"/>';
 case "lever":return '<path d="M12 76 88 76 50 30Z" fill="#d8e5ee" stroke="#67849a" stroke-width="3"/><path d="M10 30 90 70" stroke="#b77c3c" stroke-width="8" stroke-linecap="round"/>';
 case "rib_cage":return '<path d="M50 12v70" stroke="#d8dfe5" stroke-width="8" stroke-linecap="round"/><path d="M50 25q-36-17-34 8 2 16 34 17m0-25q36-17 34 8-2 16-34 17m0 0q-30 0-28 20 2 10 28 9m0-29q30 0 28 20-2 10-28 9" stroke="#e9edf0" stroke-width="6" fill="none" stroke-linecap="round"/>';
 case "plastic":return '<path d="M40 12h20v12l9 9v51H31V33l9-9Z" fill="#a9d9ee" stroke="#4c8aa8" stroke-width="3"/>';
 case "scale":return '<path d="M50 18v60M25 32h50M30 32 16 62h28ZM70 32 56 62h28Z" stroke="#607b91" stroke-width="4" fill="none"/>';
 case "cake":return '<path d="M15 42h70v39H15Z" fill="#e8a0a8" stroke="#a75e69" stroke-width="3"/><path d="M15 42q35-25 70 0" fill="#fff0d8" stroke="#a75e69" stroke-width="3"/>';
 case "tap":return '<path d="M20 37h45q15 0 15 15v7H63v-6q0-5-5-5H20Z" fill="#9fb6c7" stroke="#607b91" stroke-width="4"/><path d="M35 37V20h35v17" stroke="#607b91" stroke-width="4" fill="none"/><path d="M72 62q-8 12 0 20 8-8 0-20Z" fill="#48a9d7"/>';
 case "bin":return '<path d="M26 28h48l-5 59H31Z" fill="#67a8c7" stroke="#3b728f" stroke-width="3"/><path d="M20 25h60M39 18h22" stroke="#3b728f" stroke-width="5" stroke-linecap="round"/>';
 case "polar_bear":return body("bear");
 case "water_vapour":return body("cloud");
 case "food_chain":return body("plant");
 case "life_cycle":return body("butterfly");
 case "skeleton":return body("rib_cage");
 case "gravity":return body("planet");
 case "photosynthesis":return body("plant");
 case "day_night":return body("planet");
 case "balanced_diet":return body("plate");
 case "recycling":return body("bin");
 case "predator":return body("fox");
 case "prey":return body("rabbit");
 case "insect":return body("bee");
 case "habitat":return body("tree");
 case "evaporation":return body("water");
 case "water_cycle":return body("cloud");
 case "mirror":return body("glass");
 case "coat":return body("raincoat");
 case "cold":return body("ice");
 case "potato":return '<ellipse cx="50" cy="54" rx="38" ry="25" transform="rotate(-18 50 54)" fill="#b78a56" stroke="#805d37" stroke-width="3"/><circle cx="32" cy="45" r="2.5" fill="#805d37"/><circle cx="61" cy="62" r="2.5" fill="#805d37"/><circle cx="70" cy="43" r="2" fill="#805d37"/>';
 case "cotton":return '<path d="M50 85V42m0 22-22-15m22 4 22-18" stroke="#4f8e4d" stroke-width="5" fill="none" stroke-linecap="round"/><circle cx="28" cy="47" r="13" fill="#fff" stroke="#b9c6cf" stroke-width="2"/><circle cx="50" cy="42" r="14" fill="#fff" stroke="#b9c6cf" stroke-width="2"/><circle cx="73" cy="34" r="13" fill="#fff" stroke="#b9c6cf" stroke-width="2"/>';
 case "bean":return '<path d="M15 55q12-40 57-25 25 8 10 33-18 29-55 13-18-8-12-21Z" fill="#77b95c" stroke="#397b3d" stroke-width="3"/><ellipse cx="37" cy="54" rx="8" ry="12" fill="#c6d98a"/><ellipse cx="58" cy="54" rx="8" ry="12" fill="#c6d98a"/>';
 case "magnet":return '<path d="M22 20v38q0 30 28 30t28-30V20H59v38q0 10-9 10t-9-10V20Z" fill="#e75b58" stroke="#963c3a" stroke-width="4"/><path d="M22 20h19v18H22Zm37 0h19v18H59Z" fill="#e6edf2" stroke="#607b91" stroke-width="3"/>';
 case "soap":return '<rect x="18" y="34" width="64" height="43" rx="14" fill="#b8dff1" stroke="#6d9db6" stroke-width="3"/><path d="M29 47q20-12 40 0" stroke="#fff" stroke-width="4" fill="none" stroke-linecap="round"/>';
 case "toothbrush":return '<path d="M20 72 76 31" stroke="#4d91bd" stroke-width="10" stroke-linecap="round"/><path d="m68 24 16-12 11 15-16 12Z" fill="#e8f4fb" stroke="#4d91bd" stroke-width="3"/><path d="m72 23 8-7m-2 13 8-7m-1 13 8-7" stroke="#4d91bd" stroke-width="2"/>';
 case "helmet":return '<path d="M15 65q0-39 35-46 35 7 35 46H15Z" fill="#f2c34e" stroke="#a77c26" stroke-width="3"/><path d="M10 65h80v10H10Z" fill="#e2a936" stroke="#a77c26" stroke-width="3"/>';
 case "boat":return '<path d="M12 62h76L72 83H30Z" fill="#b87943" stroke="#80512f" stroke-width="3"/><path d="M49 18v44M49 20 22 55h27Z" fill="#f7f4e8" stroke="#8098a8" stroke-width="3"/><path d="M49 22 78 55H49Z" fill="#e7b653" stroke="#9a742f" stroke-width="3"/><path d="M8 91q14-9 28 0t28 0 28 0" stroke="#4c9fc7" stroke-width="4" fill="none"/>';
 case "shirt":return '<path d="m30 18 20 9 20-9 21 17-13 17-10-8v42H32V44l-10 8L9 35Z" fill="#6ca8d5" stroke="#3c6e97" stroke-width="3"/>';
 case "stone":return body("rock");
 case "solid":return body("block");
 case "herbivore":return body("rabbit");
 case "mammal":return body("goat");
 case "desert":return body("camel");
 case "material":return body("wood");
 case "animal_needs":return body("water");
 case "body_parts":return body("skull");
 case "safety":return body("helmet");
 case "floating":return body("boat");
 case "melting":return body("ice");
 case "circulation":return body("heart");
 case "hygiene":return body("soap");
 case "simple_machine":return body("ramp");
 case "bean_growth":return body("bean");
 case "potato_plant":return body("potato");
 case "pentagon":return '<polygon points="50,10 90,40 75,88 25,88 10,40" fill="#f2c34e" stroke="#9a742f" stroke-width="4"/>';
 case "hexagon":return '<polygon points="30,12 70,12 92,50 70,88 30,88 8,50" fill="#8bc6df" stroke="#477e98" stroke-width="4"/>';
 case "turtle":return '<ellipse cx="48" cy="59" rx="30" ry="22" fill="#7ebc5b" stroke="#3f7b3d" stroke-width="3"/><path d="M58 42q18-18 31-4 9 12-2 24-10 10-26 2Z" fill="#8dcb68" stroke="#3f7b3d" stroke-width="3"/><circle cx="80" cy="48" r="3" fill="#263b4d"/><path d="M25 75 18 86m25-5-2 10m26-12 6 9m-45-50q8-12 22-10" stroke="#3f7b3d" stroke-width="5" stroke-linecap="round" fill="none"/><path d="M35 49q12-10 26 0m-26 10q12-10 26 0" stroke="#4f8d47" stroke-width="3" fill="none"/>';
 case "star":return '<path d="m50 7 11 27 29 2-22 18 7 29-25-16-25 16 7-29L10 36l29-2Z" fill="#ffd34e" stroke="#d39a20" stroke-width="3" stroke-linejoin="round"/><path d="m39 36 3-7m15 13 7 1" stroke="#fff2b5" stroke-width="3" stroke-linecap="round"/>';
 case "cat":return '<path d="M23 39 19 12l22 15q9-4 18 0l22-15-4 29" fill="#f3b76c" stroke="#895a32" stroke-width="4" stroke-linejoin="round"/><ellipse cx="50" cy="54" rx="31" ry="28" fill="#f6c987" stroke="#895a32" stroke-width="3"/><ellipse cx="39" cy="49" rx="4" ry="6" fill="#263b4d"/><ellipse cx="61" cy="49" rx="4" ry="6" fill="#263b4d"/><path d="m45 61 5 4 5-4m-10 5q-8 8-16 1m26-1q8 8 16 1" fill="none" stroke="#895a32" stroke-width="2.5" stroke-linecap="round"/><path d="M20 61 5 57m16 10L5 71m74-10 16-4M79 67l16 4" stroke="#895a32" stroke-width="2"/>';
 case "dog":return '<path d="M26 34Q4 10 12 55q4 19 23 8M74 34Q96 10 88 55q-4 19-23 8" fill="#a96a3c" stroke="#744324" stroke-width="3"/><ellipse cx="50" cy="53" rx="31" ry="30" fill="#d89a62" stroke="#744324" stroke-width="3"/><ellipse cx="39" cy="47" rx="4" ry="5" fill="#263b4d"/><ellipse cx="61" cy="47" rx="4" ry="5" fill="#263b4d"/><ellipse cx="50" cy="62" rx="13" ry="10" fill="#f8e2c8"/><ellipse cx="50" cy="58" rx="5" ry="4" fill="#263b4d"/><path d="M50 62v7q-8 8-14 0m14 0q8 8 14 0" fill="none" stroke="#744324" stroke-width="2.5" stroke-linecap="round"/>';
 case "bird":return '<ellipse cx="48" cy="52" rx="28" ry="22" fill="#55b9df" stroke="#287d9e" stroke-width="3"/><path d="M65 46 91 36 77 58Z" fill="#f4a83a" stroke="#b77822" stroke-width="2"/><path d="M28 49q15-17 31 4-15 18-31-4Z" fill="#2f91b8" stroke="#287d9e" stroke-width="2"/><circle cx="63" cy="42" r="4" fill="#24394a"/><path d="M43 73v9m12-9v9m-17 0h10m3 0h10" stroke="#b77822" stroke-width="3" stroke-linecap="round"/>';
 case "fish":return '<path d="M15 49Q48 12 78 49 48 86 15 49Z" fill="#50b6d9" stroke="#277d9c" stroke-width="3"/><path d="m76 49 18-17v34Z" fill="#f3a13c" stroke="#b97726" stroke-width="3"/><circle cx="34" cy="44" r="4" fill="#24394a"/><path d="M47 28q-5 20 0 42" stroke="#277d9c" stroke-width="2" fill="none"/>';
 case "bee":return '<ellipse cx="50" cy="52" rx="23" ry="18" fill="#ffd34e" stroke="#9e741e" stroke-width="3"/><path d="M39 36v32m13-34v36" stroke="#3d3a32" stroke-width="7"/><ellipse cx="36" cy="28" rx="12" ry="17" fill="#d9f1ff" stroke="#7eb8d7" stroke-width="2" transform="rotate(-30 36 28)"/><ellipse cx="62" cy="28" rx="12" ry="17" fill="#d9f1ff" stroke="#7eb8d7" stroke-width="2" transform="rotate(30 62 28)"/><circle cx="72" cy="49" r="3" fill="#263b4d"/><path d="M78 44q10-10 12-2" fill="none" stroke="#6b4b29" stroke-width="2"/>';
 case "butterfly":return '<path d="M49 46Q13 5 13 43q0 22 35 19Q10 92 35 84q14-5 15-31Q51 82 66 84q25 8-13-22 35 3 34-19Q87 5 51 46Z" fill="#b99cf5" stroke="#7254b2" stroke-width="3"/><path d="M50 36v38" stroke="#4b3c68" stroke-width="5" stroke-linecap="round"/><path d="M49 37q-12-17-17-9m19 9q12-17 17-9" fill="none" stroke="#4b3c68" stroke-width="2"/>';
 case "sun":return '<circle cx="50" cy="47" r="22" fill="#ffd34e" stroke="#d99b23" stroke-width="3"/><g stroke="#e5a52b" stroke-width="5" stroke-linecap="round"><path d="M50 7v10M50 77v10M10 47h10M80 47h10M22 19l8 8m40 40 8 8M78 19l-8 8M30 67l-8 8"/></g><path d="M39 48q3 5 6 0m10 0q3 5 6 0" fill="none" stroke="#9b6b1b" stroke-width="2.5"/>';
 case "moon":return '<path d="M67 12Q27 18 29 51q2 32 35 31-25-12-21-37 3-23 24-33Z" fill="#f5cf55" stroke="#c29b34" stroke-width="3"/><circle cx="66" cy="27" r="3" fill="#f5cf55"/><circle cx="80" cy="55" r="2.5" fill="#f5cf55"/>';
 case "cloud":return '<path d="M23 68Q9 66 12 50q3-14 19-13 6-24 29-19 16 3 18 21 17-1 18 15 1 15-17 15Z" fill="#d7eaf8" stroke="#7fa9c7" stroke-width="3"/><path d="M34 49q5-6 10 0m12 0q5-6 10 0" fill="none" stroke="#5f849f" stroke-width="2"/>';
 case "rain":return '<path d="M22 47Q9 45 13 31q4-13 18-11 8-20 28-13 14 4 15 20 17-2 17 12 0 10-14 10Z" fill="#b7d7ed" stroke="#6b9ab9" stroke-width="3"/><path d="m31 58-5 13m25-13-5 13m25-13-5 13" stroke="#3999d0" stroke-width="5" stroke-linecap="round"/>';
 case "wind":return '<path d="M12 32h61q13 0 10-10-3-8-12-3M9 49h76q13 0 10 11-3 8-12 3M18 67h38" fill="none" stroke="#55a9d5" stroke-width="6" stroke-linecap="round"/>';
 case "flower":return '<path d="M50 45v37m0-16q-18-16-27-3 10 17 27 8m0-5q16-18 27-7-7 17-27 11" fill="#47a96a" stroke="#267b49" stroke-width="3"/><circle cx="50" cy="39" r="10" fill="#ffd34e" stroke="#c99b26" stroke-width="2"/><g fill="#f58bb0" stroke="#c65b85" stroke-width="2"><ellipse cx="50" cy="20" rx="10" ry="14"/><ellipse cx="68" cy="36" rx="14" ry="10"/><ellipse cx="61" cy="55" rx="10" ry="14"/><ellipse cx="39" cy="55" rx="10" ry="14"/><ellipse cx="32" cy="36" rx="14" ry="10"/></g><circle cx="50" cy="39" r="9" fill="#ffd34e"/>';
 case "plant":return '<path d="M50 70V31m0 23Q24 20 18 39q7 20 32 15m0-10q26-30 33-10-8 19-33 17" fill="none" stroke="#348b50" stroke-width="5" stroke-linecap="round"/><path d="M27 68h46l-7 18H34Z" fill="#d98258" stroke="#9c5638" stroke-width="3"/><path d="M35 68q15-7 30 0" fill="none" stroke="#87563b" stroke-width="3"/>';
 case "tree":return '<path d="M45 56h16v31H45Z" fill="#99613b" stroke="#70452d" stroke-width="3"/><circle cx="35" cy="38" r="21" fill="#64bd72" stroke="#37864a" stroke-width="3"/><circle cx="61" cy="30" r="23" fill="#55ad65" stroke="#37864a" stroke-width="3"/><circle cx="70" cy="48" r="18" fill="#78cb79" stroke="#37864a" stroke-width="3"/>';
 case "seed":return '<ellipse cx="50" cy="54" rx="22" ry="14" transform="rotate(-30 50 54)" fill="#a8753e" stroke="#704b29" stroke-width="3"/><path d="M50 46q-3-23 16-30m-15 30q-19-14-26-1" fill="none" stroke="#48a45b" stroke-width="4" stroke-linecap="round"/><path d="M66 16q18-7 17 8-10 8-17-8Z" fill="#69bd73" stroke="#34834b" stroke-width="2"/>';
 case "water":return '<path d="M50 8Q22 46 24 61a26 26 0 0 0 52 0Q78 46 50 8Z" fill="#63c6ee" stroke="#2589b8" stroke-width="3"/><path d="M38 59q2 13 13 15" fill="none" stroke="#d9f7ff" stroke-width="5" stroke-linecap="round"/>';
 case "pencil":return '<path d="m25 72 43-52q7-8 15-1 8 7 1 15L41 86l-20 4Z" fill="#ffd34e" stroke="#a77b22" stroke-width="3"/><path d="m21 90 7-20 13 16Z" fill="#f1d1a6" stroke="#8e6845" stroke-width="2"/><path d="m69 20 15 14" stroke="#fff1a6" stroke-width="4"/>';
 case "book":return '<path d="M12 23q23-8 38 6v53Q32 68 12 77Zm76 0q-23-8-38 6v53q18-14 38-5Z" fill="#70b8e7" stroke="#326c9a" stroke-width="3"/><path d="M50 29v53M21 36q13-4 22 2m36-2q-13-4-22 2" stroke="#eaf7ff" stroke-width="3" fill="none"/>';
 case "car":return '<path d="M12 55h76l-7-21q-3-8-13-8H36q-9 0-13 9Z" fill="#ed7565" stroke="#a84942" stroke-width="3"/><path d="M31 28h17v20H22Zm23 0h15q7 0 10 8l4 12H54Z" fill="#bfe7f7" stroke="#5d8ca8" stroke-width="2"/><path d="M10 55h80v16H10Z" fill="#e95e50" stroke="#a84942" stroke-width="3"/><circle cx="29" cy="73" r="9" fill="#35485b" stroke="#182c3e" stroke-width="3"/><circle cx="72" cy="73" r="9" fill="#35485b" stroke="#182c3e" stroke-width="3"/>';
 case "bus":return '<rect x="12" y="18" width="76" height="56" rx="10" fill="#ffd34e" stroke="#aa7a1e" stroke-width="3"/><path d="M20 28h15v19H20Zm21 0h15v19H41Zm21 0h18v19H62Z" fill="#c8edfb" stroke="#7199ad" stroke-width="2"/><path d="M15 55h70" stroke="#d18d1c" stroke-width="4"/><circle cx="29" cy="76" r="8" fill="#35485b"/><circle cx="72" cy="76" r="8" fill="#35485b"/>';
 case "plane":return '<path d="m8 52 34-8 18-31q5-7 10-3l-8 32 27 8q7 3 0 8l-29 3-9 24-7-1 1-23-34-2Z" fill="#c4e5f7" stroke="#5383a4" stroke-width="3" stroke-linejoin="round"/>';
 case "boat":return '<path d="M13 59h74L72 79H30Z" fill="#e98b5b" stroke="#985333" stroke-width="3"/><path d="M48 16v42H23Z" fill="#f7d45b" stroke="#a88329" stroke-width="2"/><path d="M54 23v35h24Z" fill="#79bce7" stroke="#467f9f" stroke-width="2"/><path d="M10 85q10-8 20 0t20 0 20 0 20 0" fill="none" stroke="#429acb" stroke-width="4"/>';
 case "ball":return '<circle cx="50" cy="48" r="34" fill="#f5f7fa" stroke="#344b60" stroke-width="3"/><path d="m50 28 17 12-7 20H40l-7-20Z" fill="#333f50" stroke="#263748" stroke-width="2"/><path d="m50 14 17 26m0 20-17 22M33 40 16 31m24 29-8 21m28-21 24 4" stroke="#344b60" stroke-width="3" fill="none"/>';
 case "hand":return '<path d="M28 53V24q0-7 7-7t7 7v21V12q0-7 7-7t7 7v33V17q0-7 7-7t7 7v33V27q0-7 7-7t7 7v34q0 24-22 29H43q-12-3-19-17L12 54q-4-7 2-11 6-3 10 4Z" fill="#f3bf92" stroke="#a96e49" stroke-width="3" stroke-linejoin="round"/>';
 case "plate":return '<ellipse cx="50" cy="49" rx="39" ry="28" fill="#eaf5fb" stroke="#8caec2" stroke-width="4"/><ellipse cx="50" cy="49" rx="27" ry="17" fill="#fff" stroke="#b6cbd8" stroke-width="3"/>';
 case "coin":return '<circle cx="50" cy="48" r="34" fill="#f8d36a" stroke="#ba8b2b" stroke-width="4"/><circle cx="50" cy="48" r="25" fill="none" stroke="#e5ad3e" stroke-width="3"/><path d="M50 29v38m-10-29q2-8 11-8 11 0 11 9 0 8-12 9-11 2-11 10 0 8 12 8 10 0 12-8" fill="none" stroke="#a77a27" stroke-width="3"/>';
 case "rock":return '<path d="m17 67 8-34 20-17 29 8 13 30-16 23-39 2Z" fill="#9da9b3" stroke="#64727f" stroke-width="3"/><path d="m26 39 20-17 8 22-20 14m20-14 20-12 8 22-24 8" fill="none" stroke="#d5dde3" stroke-width="3"/>';
 case "toy":return '<rect x="22" y="32" width="56" height="48" rx="12" fill="#e7a1bd" stroke="#a85d7b" stroke-width="3"/><circle cx="50" cy="24" r="20" fill="#d9a77f" stroke="#966b4c" stroke-width="3"/><circle cx="42" cy="22" r="3" fill="#35485b"/><circle cx="58" cy="22" r="3" fill="#35485b"/><path d="M43 32q7 6 14 0" fill="none" stroke="#8a5942" stroke-width="2.5"/><path d="M23 39 10 25m67 14 13-14" stroke="#a85d7b" stroke-width="7" stroke-linecap="round"/>';
 case "egg":return '<path d="M50 10C30 10 20 47 24 67q4 20 26 18 22-1 26-20 4-19-10-43Q58 10 50 10Z" fill="#fff8e7" stroke="#c8b99a" stroke-width="3"/><path d="M37 53q4-11 10-7" fill="none" stroke="#fff" stroke-width="4" stroke-linecap="round"/>';
 case "carrot":return '<path d="m28 28 48 8-28 48q-7 10-11-1Z" fill="#f28d3d" stroke="#b85c20" stroke-width="3"/><path d="M49 29Q36 8 25 17q-4 11 22 16m3-5Q49 5 64 7q8 10-10 25m-3-1Q68 8 80 20q2 12-27 17" fill="#58b96d" stroke="#2e8748" stroke-width="3"/><path d="m40 51 10 3m-5 12 10 3" stroke="#ffbd79" stroke-width="3"/>';
 case "tomato":return '<circle cx="50" cy="53" r="29" fill="#ef6658" stroke="#a83c37" stroke-width="3"/><path d="m50 26 6-12 5 12 13-4-9 12-15-3-15 3-9-12 13 4Z" fill="#45a75d" stroke="#2b7c40" stroke-width="2"/>';
 case "heart":return '<path d="M50 82 16 49Q2 27 22 17q17-8 28 10 11-18 28-10 20 10 6 32Z" fill="#ed6a7c" stroke="#a83d55" stroke-width="3"/><path d="M25 30q8-7 15 0" stroke="#ffb9c3" stroke-width="4" fill="none" stroke-linecap="round"/>';
 case "hat":return '<path d="M20 53q4-30 30-35 28 5 30 35Z" fill="#5e9fd1" stroke="#35698f" stroke-width="3"/><path d="M11 55q39-15 78 0 3 12-39 13-43-1-39-13Z" fill="#397eae" stroke="#285a80" stroke-width="3"/>';
 case "sock":return '<path d="M32 13h35v42q0 9 15 11 11 2 8 13-3 10-20 8H29q-13-1-10-15l13-17Z" fill="#f1a7c2" stroke="#a75f7c" stroke-width="3"/><path d="M32 26h35M32 35h35" stroke="#fff" stroke-width="5"/>';
 case "shoe":return '<path d="M19 22h28q0 26 18 34l20 7q9 3 6 14-2 7-12 7H17q-9-1-7-11l9-19Z" fill="#5da5d8" stroke="#316b99" stroke-width="3"/><path d="M18 64q28 6 48 0" stroke="#fff" stroke-width="5"/>';
 case "bed":return '<path d="M15 28v48m0-15h70v15m-70-28h70q7 0 7 8v20H15Z" fill="#8fc4e6" stroke="#477b9e" stroke-width="3"/><rect x="21" y="39" width="26" height="16" rx="6" fill="#fff" stroke="#9cb6c7" stroke-width="2"/><path d="M14 77v9m73-9v9" stroke="#6e533f" stroke-width="5"/>';
 case "school":return '<path d="M12 37 50 12l38 25v47H12Z" fill="#f2c76a" stroke="#a77a2d" stroke-width="3"/><path d="M41 84V55h18v29M22 46h13v14H22m43-14h13v14H65" fill="#bfe5f7" stroke="#6c98b3" stroke-width="2"/><path d="M50 12V4m-7 0h14" stroke="#b94b46" stroke-width="4"/>';
 case "teacher":return '<circle cx="50" cy="25" r="17" fill="#d9a27b" stroke="#96684a" stroke-width="3"/><path d="M34 22q2-19 19-17 15 3 15 17-14-7-34 0Z" fill="#4b3c36"/><path d="M22 83q2-34 28-34t28 34Z" fill="#5c98c7" stroke="#37688e" stroke-width="3"/><path d="M60 58h27v18H60" fill="#fff" stroke="#7b9bb2" stroke-width="2"/>';
 case "window":return '<rect x="16" y="12" width="68" height="66" rx="5" fill="#bfe8f7" stroke="#8c6848" stroke-width="6"/><path d="M50 13v64M17 45h66" stroke="#8c6848" stroke-width="5"/><circle cx="34" cy="30" r="8" fill="#fff2b4"/>';
 case "robot":return '<rect x="23" y="24" width="54" height="48" rx="10" fill="#9dc8e5" stroke="#4e7c9b" stroke-width="3"/><rect x="31" y="9" width="38" height="20" rx="7" fill="#d5e8f3" stroke="#4e7c9b" stroke-width="3"/><circle cx="41" cy="43" r="5" fill="#263b4d"/><circle cx="59" cy="43" r="5" fill="#263b4d"/><path d="M41 58h18M50 9V3m-7 0h14M23 41H9m68 0h14M37 72v12m26-12v12" stroke="#4e7c9b" stroke-width="4" stroke-linecap="round"/>';
 case "nest":return '<path d="M15 58q35 34 70 0-6 29-35 29T15 58Z" fill="#b97a42" stroke="#81532e" stroke-width="3"/><path d="M20 61q30 18 60 0M25 70q25 15 50 0" stroke="#e3b17c" stroke-width="3" fill="none"/><ellipse cx="50" cy="48" rx="13" ry="17" fill="#f1d9a7" stroke="#b89a67" stroke-width="2"/><circle cx="45" cy="44" r="2" fill="#333"/>';
 case "map":return '<path d="m12 25 25-10 26 10 25-10v58L63 83 37 73 12 83Z" fill="#d4edc8" stroke="#5b9860" stroke-width="3"/><path d="M37 15v58M63 25v58" stroke="#7eaa79" stroke-width="3"/><path d="m20 60 18-16 14 9 18-22" stroke="#e28b55" stroke-width="5" fill="none" stroke-linecap="round"/><path d="M72 27q0-10 9-10t9 10q0 8-9 17-9-9-9-17Z" fill="#ed6a62" stroke="#a74442" stroke-width="2"/>';
 case "circle":return '<circle cx="50" cy="48" r="31" fill="#78b9e5" stroke="#367cae" stroke-width="4"/>';
 case "square":return '<rect x="19" y="17" width="62" height="62" rx="3" fill="#f4a36f" stroke="#b9683b" stroke-width="4"/>';
 case "triangle":return '<path d="M50 13 89 79H11Z" fill="#8fd1a0" stroke="#438c58" stroke-width="4" stroke-linejoin="round"/>';
 case "rectangle":return '<rect x="10" y="28" width="80" height="43" rx="3" fill="#a89be8" stroke="#6657a6" stroke-width="4"/>';
 case "time":case "clock":return '<circle cx="50" cy="48" r="36" fill="#fff" stroke="#4b667d" stroke-width="5"/><path d="M50 17v7m31 24h-7m-24 31v-7M19 48h7M50 48V25m0 23 19 11" stroke="#36536d" stroke-width="4" stroke-linecap="round"/><circle cx="50" cy="48" r="4" fill="#e56f62"/>';
 case "number":return '<rect x="15" y="12" width="70" height="68" rx="15" fill="#e7f2fb" stroke="#86a9c2" stroke-width="3"/><text x="50" y="62" text-anchor="middle" font-size="43" font-weight="900" fill="#356d99">#</text>';
 case "letter":return '<rect x="15" y="12" width="70" height="68" rx="15" fill="#fff2d4" stroke="#d8b05b" stroke-width="3"/><text x="50" y="62" text-anchor="middle" font-size="43" font-weight="900" fill="#9b7024">A</text>';
 case "spoon":return '<ellipse cx="50" cy="25" rx="14" ry="20" fill="#c9d9e3" stroke="#728b9a" stroke-width="3"/><path d="M50 43v43" stroke="#9eafba" stroke-width="8" stroke-linecap="round"/>';
 case "umbrella":return '<path d="M10 47Q50-4 90 47Z" fill="#f2a0a5" stroke="#a85e6b" stroke-width="3"/><path d="M50 47v32q0 12 10 8" fill="none" stroke="#77583f" stroke-width="5" stroke-linecap="round"/><path d="M30 45q0 11 10 11t10-11q0 11 10 11t10-11" fill="none" stroke="#a85e6b" stroke-width="2"/>';
 case "bear":return '<circle cx="28" cy="26" r="13" fill="#9b6847" stroke="#67432f" stroke-width="3"/><circle cx="72" cy="26" r="13" fill="#9b6847" stroke="#67432f" stroke-width="3"/><ellipse cx="50" cy="54" rx="34" ry="31" fill="#b27a52" stroke="#67432f" stroke-width="3"/><ellipse cx="50" cy="65" rx="17" ry="12" fill="#e9c39e"/><circle cx="39" cy="48" r="4" fill="#263b4d"/><circle cx="61" cy="48" r="4" fill="#263b4d"/>';
 case "elephant":return '<path d="M17 48q0-29 33-29 35 0 35 29v22H68V54q-8 8-17 4v25H39V57q-22 7-22-9Z" fill="#9caeb9" stroke="#627783" stroke-width="3"/><path d="M78 46q15 3 12 20-2 14-13 14" fill="none" stroke="#627783" stroke-width="9" stroke-linecap="round"/><circle cx="65" cy="40" r="3" fill="#263b4d"/>';
 case "group":return '<circle cx="30" cy="34" r="13" fill="#e6b38f" stroke="#93674b" stroke-width="2"/><circle cx="50" cy="27" r="14" fill="#d9a17c" stroke="#93674b" stroke-width="2"/><circle cx="71" cy="34" r="13" fill="#e6b38f" stroke="#93674b" stroke-width="2"/><path d="M10 78q0-25 20-25t20 25m-5 0q0-31 25-31t25 31" fill="#78b2d8" stroke="#477c9f" stroke-width="3"/>';
 case "ruler":return '<rect x="10" y="31" width="80" height="34" rx="4" fill="#f6d477" stroke="#b58a35" stroke-width="3"/><path d="M20 31v15m10-15v9m10-9v15m10-15v9m10-9v15m10-15v9m10-9v15" stroke="#80652e" stroke-width="2"/><text x="20" y="59" font-size="10" fill="#775c2a">0</text><text x="80" y="59" font-size="10" fill="#775c2a">6</text>';
 case "magnet":return '<path d="M27 18v34q0 31 23 31t23-31V18H57v34q0 9-7 9t-7-9V18Z" fill="#e96d67" stroke="#a84546" stroke-width="3"/><path d="M27 18h16v20H27Zm30 0h16v20H57Z" fill="#c5d7e2" stroke="#728c9b" stroke-width="3"/>';
 case "lungs":return '<path d="M48 13v34m4-34v34" stroke="#66899e" stroke-width="6" stroke-linecap="round"/><path d="M45 40q-12-23-22-4-11 20-9 37 4 19 22 9 14-7 14-29m5-13q12-23 22-4 11 20 9 37-4 19-22 9-14-7-14-29" fill="#e98e99" stroke="#a94f65" stroke-width="3"/>';
 case "canvas":return '<rect x="12" y="12" width="76" height="68" rx="5" fill="#fff" stroke="#9b7956" stroke-width="5"/><path d="M20 66 40 43l13 13 13-21 15 31Z" fill="#8cc58e" stroke="#4c8d5b" stroke-width="2"/><circle cx="66" cy="29" r="8" fill="#ffd34e"/>';
 case "ant":return '<ellipse cx="50" cy="52" rx="12" ry="10" fill="#9a5b35" stroke="#59351f" stroke-width="3"/><circle cx="30" cy="49" r="10" fill="#b87342" stroke="#59351f" stroke-width="3"/><ellipse cx="72" cy="53" rx="16" ry="13" fill="#7f492f" stroke="#59351f" stroke-width="3"/><path d="m39 57-12 16m20-10-7 19m14-19 9 19m8-19 13 15M26 42 17 28m17 12 6-15m-6 0q-12-13-18-3" stroke="#59351f" stroke-width="3" fill="none" stroke-linecap="round"/>';
 case "top":return '<path d="M50 12v18m-5-9h10M50 30 27 57q-8 12 5 20 18 11 36 0 13-8 5-20Z" fill="#ed7c61" stroke="#a64d43" stroke-width="3"/><path d="M32 55h36M38 67h24" stroke="#ffd45b" stroke-width="5"/><path d="M50 78v12" stroke="#805c3c" stroke-width="5"/>';
 case "tin":return '<rect x="25" y="20" width="50" height="62" rx="7" fill="#c9d7df" stroke="#708795" stroke-width="3"/><ellipse cx="50" cy="21" rx="25" ry="8" fill="#eaf1f5" stroke="#708795" stroke-width="3"/><path d="M34 42h32m-32 9h32" stroke="#8da3b0" stroke-width="3"/>';
 case "bat":return '<path d="M25 77 70 20q8-9 16-2 8 7 0 16L39 88q-8 7-14-1Z" fill="#d39a5a" stroke="#865b32" stroke-width="3"/><path d="m65 27 15 12" stroke="#f4d6a6" stroke-width="4"/>';
 case "pen":return '<path d="m34 77 34-56q7-10 15-4 8 5 2 15L49 87l-19 6Z" fill="#579fd5" stroke="#2d6d9e" stroke-width="3"/><path d="m30 93 4-16 15 10Z" fill="#c9d6de" stroke="#718796" stroke-width="2"/><path d="m68 21 14 9" stroke="#e7f5ff" stroke-width="4"/>';
 case "pan":return '<ellipse cx="43" cy="49" rx="30" ry="22" fill="#b9c8d1" stroke="#617a89" stroke-width="4"/><path d="M69 45 94 29q7-4 9 3 1 5-5 8L74 57" fill="#8a9eaa" stroke="#617a89" stroke-width="4"/><ellipse cx="43" cy="49" rx="21" ry="13" fill="#f1d7a2"/>';
 case "person":return '<circle cx="50" cy="25" r="17" fill="#d8a17c" stroke="#93674b" stroke-width="3"/><path d="M33 23q3-20 20-18 15 2 17 19-14-8-37-1Z" fill="#493b35"/><path d="M20 87q2-36 30-36t30 36Z" fill="#6da8d3" stroke="#47789b" stroke-width="3"/><path d="M39 44q11 8 22 0" fill="none" stroke="#80533e" stroke-width="2"/>';
 case "ear":return '<path d="M60 82q-20 8-23-13-2-12-12-22-10-12-2-28 10-21 33-14 24 7 19 32-3 14-15 23-8 6-7 15Z" fill="#e9b18d" stroke="#a86e4d" stroke-width="4"/><path d="M57 68q-8-8 4-18 13-12 6-23-8-13-21-5-7 4-7 13" fill="none" stroke="#a86e4d" stroke-width="4" stroke-linecap="round"/>';
 case "nose":return '<path d="M48 15q-2 30-16 49-8 13 9 18 14 4 26-4 9-7-2-14-8-5-9-13" fill="#e7b18d" stroke="#a66f50" stroke-width="4" stroke-linejoin="round"/>';
 case "eye":case "sight":return '<path d="M8 48Q50 5 92 48 50 91 8 48Z" fill="#fff" stroke="#6088a4" stroke-width="4"/><circle cx="50" cy="48" r="19" fill="#65a8d1" stroke="#3b6f91" stroke-width="3"/><circle cx="50" cy="48" r="8" fill="#263b4d"/><circle cx="57" cy="40" r="4" fill="#fff"/>';
 case "taste":return '<path d="M18 48q32-24 64 0-7 27-32 29-25-2-32-29Z" fill="#e77e91" stroke="#a94e68" stroke-width="3"/><path d="M50 48v27" stroke="#a94e68" stroke-width="3"/>';
 case "mat":return '<path d="M16 27q33-18 68 0v48q-35 18-68 0Z" fill="#8bc3a0" stroke="#4d8b67" stroke-width="3"/><path d="M25 34v34m12-38v42m12-43v44m12-43v42m12-38v34" stroke="#d8f0dc" stroke-width="3"/>';
 case "net":return '<path d="M18 15v68m64-68v68M18 23h64M18 43h64M18 63h64M18 83h64M18 15l64 68M82 15 18 83" stroke="#5593b4" stroke-width="3" fill="none"/>';
 case "van":return '<path d="M12 33q0-8 10-8h40q10 0 15 10l11 21v17H12Z" fill="#8dc4e7" stroke="#4d7e9d" stroke-width="3"/><path d="M22 34h31v19H22Zm36 0q9 0 13 9l4 10H58Z" fill="#e5f7ff" stroke="#4d7e9d" stroke-width="2"/><circle cx="30" cy="75" r="9" fill="#35485b"/><circle cx="73" cy="75" r="9" fill="#35485b"/>';
 case "jug":return '<path d="M35 23h31v10q16 6 16 22v25q0 8-9 8H32q-9 0-9-9V52q0-15 12-19Z" fill="#9bd3eb" stroke="#5489a5" stroke-width="3"/><path d="M66 33q25-5 22 18" fill="none" stroke="#5489a5" stroke-width="7"/><path d="M34 56h38" stroke="#fff" stroke-width="4"/>';
 case "log":return '<path d="M15 31q0-10 14-10h48q12 0 12 10v35q0 12-12 12H29Q15 78 15 66Z" fill="#a96f42" stroke="#70482c" stroke-width="3"/><ellipse cx="27" cy="48" rx="12" ry="18" fill="#e0b07d" stroke="#70482c" stroke-width="3"/><ellipse cx="27" cy="48" rx="6" ry="10" fill="none" stroke="#a5784b" stroke-width="2"/><path d="M45 26q-5 18 4 45m14-45q-5 18 4 45" stroke="#7e512f" stroke-width="3" fill="none"/>';
 case "peg":return '<path d="M28 20q22 0 22 22v37H34V44q0-9-6-9Z" fill="#e5b46c" stroke="#95652f" stroke-width="3"/><path d="M50 20q22 0 22 22v37H56V44q0-9-6-9Z" fill="#f2d08d" stroke="#95652f" stroke-width="3"/><path d="M42 17h16v12H42Z" fill="#b8c8d2" stroke="#738795" stroke-width="2"/>';
 case "jam":return '<rect x="27" y="27" width="46" height="57" rx="8" fill="#d96e68" stroke="#95423f" stroke-width="3"/><rect x="24" y="17" width="52" height="13" rx="4" fill="#c8d5dc" stroke="#758b97" stroke-width="3"/><rect x="34" y="45" width="32" height="22" rx="4" fill="#fff2d7"/><path d="M40 55q10-9 20 0" stroke="#bd645d" stroke-width="3" fill="none"/>';
 case "cap":return '<path d="M17 55q6-35 35-36 31 1 34 34Z" fill="#e96c5f" stroke="#a84641" stroke-width="3"/><path d="M12 55q39-15 78 0 4 12-39 14-42-2-39-14Z" fill="#c94f49" stroke="#a84641" stroke-width="3"/><path d="M50 21v24" stroke="#f7b5a8" stroke-width="3"/>';
 case "smile":return '<circle cx="50" cy="48" r="36" fill="#ffd86c" stroke="#c99a2d" stroke-width="3"/><circle cx="38" cy="39" r="4" fill="#394b5b"/><circle cx="62" cy="39" r="4" fill="#394b5b"/><path d="M30 55q20 25 40 0" fill="none" stroke="#a75b42" stroke-width="4" stroke-linecap="round"/>';
 case "iron":case "metal":return '<path d="M18 34 73 20l13 14-55 14Z" fill="#a9bbc5" stroke="#647c8b" stroke-width="3"/><path d="M31 48v24l55-14V34" fill="#d5e0e6" stroke="#647c8b" stroke-width="3"/><path d="m44 43 25-7" stroke="#fff" stroke-width="4"/>';
 case "wood":return '<path d="M15 28h70v42H15Z" fill="#b9814e" stroke="#79502f" stroke-width="3"/><path d="M20 39q18-12 34 0t30 0m-64 18q18-12 34 0t30 0" fill="none" stroke="#e2b47d" stroke-width="4"/>';
 case "paper":return '<path d="M25 10h39l15 15v62H25Z" fill="#fff" stroke="#8da4b4" stroke-width="3"/><path d="M64 10v17h15M35 42h34M35 53h34M35 64h27" stroke="#78a5c3" stroke-width="3" fill="none"/>';
 case "continent":return '<circle cx="50" cy="48" r="37" fill="#8cc7e4" stroke="#4c85a5" stroke-width="3"/><path d="m31 23 16-8 10 10-5 11-9 3-5 14-11-3-7-14Zm28 18 12 2 9 13-9 13-11-4-7-15Z" fill="#6db778" stroke="#438b56" stroke-width="2"/>';
 case "africa":return '<path d="m41 10 24 6 11 19-7 18-10 13-8 23-12-8-7-22-12-15 6-24Z" fill="#79bd79" stroke="#43844c" stroke-width="3"/><path d="m66 78 10 6-4 7-10-4Z" fill="#79bd79" stroke="#43844c" stroke-width="2"/>';
 case "ice":return '<path d="m50 9 30 18v38L50 84 20 65V27Z" fill="#b9e6f7" stroke="#6da8c6" stroke-width="3"/><path d="m50 9 0 75M20 27l60 38M80 27 20 65" stroke="#e9f9ff" stroke-width="3"/>';
 case "dot":return '<circle cx="28" cy="30" r="9" fill="#4e9ed2"/><circle cx="50" cy="30" r="9" fill="#4e9ed2"/><circle cx="72" cy="30" r="9" fill="#4e9ed2"/><circle cx="28" cy="60" r="9" fill="#4e9ed2"/><circle cx="50" cy="60" r="9" fill="#4e9ed2"/><circle cx="72" cy="60" r="9" fill="#4e9ed2"/>';
 case "season":return '<circle cx="28" cy="27" r="13" fill="#ffd34e"/><path d="M65 17q16 5 15 20-13 9-22-2Z" fill="#70b77a" stroke="#43844d" stroke-width="2"/><path d="M23 62q20-20 36 0-18 18-36 0Z" fill="#d98d55" stroke="#a86238" stroke-width="2"/><path d="m70 56 9 15 9-15Z" fill="#bfe8f7" stroke="#6ca6c8" stroke-width="2"/>';
 case "green":case "red":case "blue":case "yellow":case "purple":case "white":case "black":case "brown":case "grey":
  var colors={green:"#35a85a",red:"#e64f54",blue:"#4c91dc",yellow:"#f3c943",purple:"#9b79d4",white:"#ffffff",black:"#303a48",brown:"#99613d",grey:"#9ba6af"};
  return '<circle cx="50" cy="48" r="32" fill="'+colors[kind]+'" stroke="#64798a" stroke-width="3"/><circle cx="39" cy="36" r="7" fill="#ffffff" opacity=".35"/>';
 case "leaf":return '<path d="M18 75Q16 20 82 13q4 61-64 62Z" fill="#6fbe79" stroke="#3a8b4d" stroke-width="3"/><path d="M20 75Q48 43 78 19M39 55l-4-18m22 1 3 16" stroke="#317a43" stroke-width="3" fill="none"/>';
 case "kite":return '<path d="M50 9 83 43 50 77 17 43Z" fill="#f08b78" stroke="#a64e48" stroke-width="3"/><path d="m50 9 0 68M17 43h66" stroke="#fff1c5" stroke-width="3"/><path d="M50 77q-12 9-2 16 10 6 0 13" fill="none" stroke="#6e91ad" stroke-width="3"/>';
 case "whale":return '<path d="M12 54q10-30 47-27 25 2 31 23l8 3-10 12q-13 17-39 12Q24 74 12 54Z" fill="#5c9ec7" stroke="#326b91" stroke-width="3"/><path d="m72 34 15-16 4 22" fill="#5c9ec7" stroke="#326b91" stroke-width="3"/><circle cx="67" cy="46" r="3" fill="#263b4d"/>';
 case "zebra":return '<path d="M23 30q0-16 27-16t27 16v39q-8 17-27 17T23 69Z" fill="#fff" stroke="#536878" stroke-width="3"/><path d="M27 29 40 18m-15 28 25-25m-25 43 45-42m-41 61 45-43" stroke="#273b4c" stroke-width="7"/><circle cx="42" cy="43" r="3" fill="#263b4d"/><circle cx="60" cy="43" r="3" fill="#263b4d"/>';
 case "grape":return '<g fill="#8b6bc5" stroke="#60458f" stroke-width="2"><circle cx="39" cy="37" r="12"/><circle cx="58" cy="37" r="12"/><circle cx="29" cy="55" r="12"/><circle cx="49" cy="56" r="12"/><circle cx="68" cy="55" r="12"/><circle cx="40" cy="74" r="12"/><circle cx="59" cy="74" r="12"/></g><path d="M49 25q-2-16 11-20" fill="none" stroke="#39894d" stroke-width="4"/>';
 case "phone":return '<rect x="27" y="7" width="46" height="82" rx="9" fill="#344b61" stroke="#1e3449" stroke-width="3"/><rect x="32" y="16" width="36" height="61" rx="3" fill="#bfe8f7"/><circle cx="50" cy="83" r="3" fill="#dbe7ee"/><path d="M38 29h24m-24 9h24m-24 9h17" stroke="#6ba2c4" stroke-width="3"/>';
 case "pig":return '<ellipse cx="50" cy="55" rx="34" ry="27" fill="#f1a8b7" stroke="#a95f72" stroke-width="3"/><path d="M24 36 18 16l23 12m18 0 23-12-6 23" fill="#eea0b2" stroke="#a95f72" stroke-width="3"/><ellipse cx="50" cy="62" rx="16" ry="11" fill="#f8c3ce" stroke="#a95f72" stroke-width="2"/><circle cx="44" cy="62" r="3" fill="#9b5264"/><circle cx="56" cy="62" r="3" fill="#9b5264"/><circle cx="39" cy="45" r="3" fill="#44333b"/><circle cx="62" cy="45" r="3" fill="#44333b"/>';
 case "train":return '<rect x="18" y="27" width="64" height="44" rx="8" fill="#e97867" stroke="#a84a43" stroke-width="3"/><rect x="28" y="35" width="17" height="15" fill="#c7eafa" stroke="#6b93aa" stroke-width="2"/><rect x="51" y="35" width="18" height="15" fill="#c7eafa" stroke="#6b93aa" stroke-width="2"/><path d="M12 71h76M32 71v9m38-9v9" stroke="#42586a" stroke-width="4"/><circle cx="32" cy="73" r="7" fill="#42586a"/><circle cx="69" cy="73" r="7" fill="#42586a"/>';
 case "rabbit":return '<ellipse cx="50" cy="56" rx="29" ry="27" fill="#eee4d8" stroke="#9e8c7c" stroke-width="3"/><ellipse cx="38" cy="19" rx="9" ry="24" transform="rotate(-12 38 19)" fill="#eee4d8" stroke="#9e8c7c" stroke-width="3"/><ellipse cx="63" cy="18" rx="9" ry="24" transform="rotate(12 63 18)" fill="#eee4d8" stroke="#9e8c7c" stroke-width="3"/><circle cx="40" cy="53" r="3" fill="#263b4d"/><circle cx="60" cy="53" r="3" fill="#263b4d"/><ellipse cx="50" cy="63" rx="7" ry="5" fill="#e98ea4"/>';
 case "fox":return '<path d="M20 36 14 12l24 15q12-5 24 0l24-15-6 24v29q-9 18-30 18T20 65Z" fill="#e98a4d" stroke="#a9552e" stroke-width="3"/><path d="M30 64q20 12 40 0" fill="#fff0dc" stroke="#a9552e" stroke-width="2"/><circle cx="40" cy="48" r="3" fill="#263b4d"/><circle cx="60" cy="48" r="3" fill="#263b4d"/>';
 case "food":return '<circle cx="50" cy="48" r="35" fill="#f4f7f8" stroke="#9aabb5" stroke-width="4"/><path d="M50 13v70M50 48h35" stroke="#9aabb5" stroke-width="3"/><path d="M20 47q12-20 27 0-15 20-27 0Z" fill="#7dbd79"/><circle cx="68" cy="30" r="10" fill="#f0b95c"/><path d="M60 63q15-12 25 2" stroke="#e07869" stroke-width="7" fill="none"/>';
 case "small":return '<circle cx="50" cy="49" r="9" fill="#9b6847" stroke="#67432f" stroke-width="2"/><path d="M50 39v-9m-7 4-8-8m22 8 8-8m-24 30-9 9m26-9 9 9" stroke="#67432f" stroke-width="2"/>';
 case "big":return '<path d="M18 55q0-33 32-33t32 33v20H18Z" fill="#9caeb9" stroke="#627783" stroke-width="3"/><path d="M75 52q20 6 11 24" fill="none" stroke="#627783" stroke-width="8"/>';
 case "loud":return '<path d="M15 38h19l26-20v60L34 58H15Z" fill="#e7a34f" stroke="#a76a2b" stroke-width="3"/><path d="M70 33q16 15 0 30m8-40q26 25 0 50" fill="none" stroke="#5a9dc6" stroke-width="5" stroke-linecap="round"/>';
 case "sticker":return '<path d="M50 10 60 31 84 34 67 51 71 76 50 64 29 76 33 51 16 34 40 31Z" fill="#f3a6c0" stroke="#ad6480" stroke-width="3"/><path d="M50 10v54" stroke="#fff" stroke-width="3" opacity=".7"/>';
 case "spring":return '<path d="M50 73V43m0 16Q25 33 18 50q8 17 32 14m0-9q23-25 33-8-8 18-33 15" fill="none" stroke="#399451" stroke-width="4"/><circle cx="50" cy="27" r="12" fill="#ffd34e"/><g fill="#f39abd"><circle cx="50" cy="11" r="7"/><circle cx="66" cy="27" r="7"/><circle cx="50" cy="43" r="7"/><circle cx="34" cy="27" r="7"/></g>';
 case "summer":return '<circle cx="50" cy="38" r="23" fill="#ffd34e" stroke="#d39a20" stroke-width="3"/><path d="M50 5v10m0 46v10M17 38h10m46 0h10M26 14l8 8m32 32 8 8m0-48-8 8M34 54l-8 8" stroke="#d39a20" stroke-width="4" stroke-linecap="round"/><path d="M10 80q20-10 40 0t40 0" fill="none" stroke="#4e9ec8" stroke-width="5"/>';
 case "autumn":return '<path d="M50 80V30m0 28Q23 17 14 36q7 23 36 19m0-10q27-27 37-7-8 20-37 17" fill="none" stroke="#8a613a" stroke-width="5"/><path d="M17 80q10-12 20 0m27 0q10-12 20 0" stroke="#d88943" stroke-width="5" fill="none"/>';
 case "winter":return '<path d="M50 8v78M16 29l68 38M16 67l68-38M35 17l15 9 15-9M35 77l15-9 15 9M16 29l16 1 1-16M84 29l-16 1-1-16" stroke="#73b7dc" stroke-width="5" stroke-linecap="round" fill="none"/>';
 case "milk":return '<path d="M34 13h32l-4 13 10 12v45H28V38l10-12Z" fill="#e8f5fb" stroke="#7197ad" stroke-width="3"/><path d="M38 13h24m-26 18h28" stroke="#7197ad" stroke-width="3"/><path d="M34 54h32" stroke="#a5d5e8" stroke-width="8"/><path d="M44 63h12" stroke="#fff" stroke-width="3"/>';
 case "camel":return '<path d="M12 66q5-22 22-22 7-26 20-26 13 0 18 25 15-4 19 23l-8 6H18Z" fill="#c99a5a" stroke="#8b623b" stroke-width="3"/><path d="M22 70v16m20-16v16m28-16v16m12-16v16M68 43q10-14 15-8l4 12" stroke="#8b623b" stroke-width="5" stroke-linecap="round"/><circle cx="83" cy="43" r="2.5" fill="#24364a"/>';
 case "goat":return '<path d="M18 62q4-25 34-25 25 0 31 23l-8 13H26Z" fill="#e6e2d8" stroke="#8b8273" stroke-width="3"/><path d="M69 42q10-20 21-11l-4 18m-22-7q-9-22-19-12l5 17" fill="none" stroke="#8b8273" stroke-width="5" stroke-linecap="round"/><path d="M29 70v15m39-15v15" stroke="#8b8273" stroke-width="5"/><path d="M81 52l10 5-10 5" fill="#e6e2d8" stroke="#8b8273" stroke-width="2"/><circle cx="83" cy="46" r="2" fill="#24364a"/>';
 case "raincoat":return '<path d="M35 18 50 10l15 8 17 18-12 12-7-8v42H37V40l-7 8-12-12Z" fill="#e6c44f" stroke="#9c812e" stroke-width="3"/><path d="M50 11v70m-10-45h20" stroke="#fff4bd" stroke-width="3"/><circle cx="57" cy="48" r="2" fill="#9c812e"/>';
 case "soap":return '<rect x="20" y="39" width="60" height="36" rx="14" fill="#9bd8e9" stroke="#4c9eb5" stroke-width="3"/><path d="M30 39q3-20 20-20t20 20" fill="#d9f3fa" stroke="#4c9eb5" stroke-width="3"/><circle cx="22" cy="20" r="7" fill="#d9f3fa" stroke="#7ebbd0" stroke-width="2"/><circle cx="78" cy="18" r="5" fill="#d9f3fa" stroke="#7ebbd0" stroke-width="2"/>';
 case "germ":return '<path d="M50 10v12m0 52v12M10 48h12m56 0h12M22 20l9 9m38 38 9 9M78 20l-9 9M31 67l-9 9" stroke="#6b9c58" stroke-width="5" stroke-linecap="round"/><circle cx="50" cy="48" r="29" fill="#9acb68" stroke="#578b42" stroke-width="3"/><circle cx="40" cy="41" r="4" fill="#35543b"/><circle cx="60" cy="41" r="4" fill="#35543b"/><path d="M39 58q11 10 22 0" stroke="#35543b" stroke-width="3" fill="none"/>';
 case "glass":return '<path d="M24 15h52l-7 65H31Z" fill="#d7f1fa" fill-opacity=".8" stroke="#579db9" stroke-width="4"/><path d="M33 50h34" stroke="#67b9d2" stroke-width="4"/><path d="M10 23l10-8m60 0 10 8M50 3v12" stroke="#e9bf4d" stroke-width="4" stroke-linecap="round"/>';
 case "skull":return '<path d="M50 10q-31 0-31 31 0 17 13 26v14h36V67q13-9 13-26Q81 10 50 10Z" fill="#f4eee0" stroke="#a69a83" stroke-width="3"/><circle cx="39" cy="42" r="9" fill="#536174"/><circle cx="61" cy="42" r="9" fill="#536174"/><path d="M50 50l-6 10h12Z" fill="#8b7d69"/><path d="M40 68v12m10-12v12m10-12v12" stroke="#a69a83" stroke-width="3"/>';
 case "brain":return '<path d="M48 17q-16-13-23 7-17 3-12 22-10 17 8 25 1 18 20 12l9-7 9 7q20 5 21-14 18-11 7-27 5-20-14-22-7-20-25-3Z" fill="#ed9da8" stroke="#b65f73" stroke-width="3"/><path d="M50 19v58M28 31q15 0 11 14m-13 8q15-4 14 12m34-34q-15 0-11 14m13 8q-15-4-14 12" stroke="#b65f73" stroke-width="3" fill="none"/>';
 case "cork":return '<rect x="34" y="17" width="32" height="61" rx="7" fill="#b98752" stroke="#805b35" stroke-width="3"/><path d="M38 30h24m-24 12h24m-24 12h24m-24 12h24" stroke="#d5ae7e" stroke-width="3"/>';
 case "stove":return '<rect x="17" y="15" width="66" height="67" rx="8" fill="#c5d0d8" stroke="#697d8e" stroke-width="3"/><circle cx="34" cy="31" r="7" fill="#e9a94c"/><circle cx="66" cy="31" r="7" fill="#e9a94c"/><rect x="28" y="48" width="44" height="25" rx="4" fill="#536a7d"/><path d="M50 66q-12-10 0-19 12 9 0 19Z" fill="#f08b43"/>';

 case "lemon":return '<ellipse cx="50" cy="51" rx="31" ry="20" transform="rotate(-24 50 51)" fill="#f4df4b" stroke="#b99a27" stroke-width="3"/><path d="M50 29q13-17 28-10-6 16-28 10Z" fill="#6ca85a" stroke="#477e43" stroke-width="3"/>';
 case "cow":return '<path d="M13 38q5-16 30-14h24q19 2 20 22v22H13Z" fill="#f8f7f1" stroke="#78808a" stroke-width="3"/><path d="M28 31q7-12 19-4l-3 13-17 2Zm35 7q12-15 20-3l-5 16-17-2Z" fill="#6b7180"/><path d="M67 49q18-13 25 4v17H65Z" fill="#f1c4c8" stroke="#8f6970" stroke-width="3"/><path d="M20 68v17m18-17v17m29-17v17m13-17v17" stroke="#78808a" stroke-width="5"/><path d="M73 29 68 15m16 15 7-13" stroke="#8b8f97" stroke-width="4" stroke-linecap="round"/><circle cx="82" cy="53" r="2" fill="#263548"/>';
 case "rainbow":return '<path d="M12 78a38 38 0 0 1 76 0" fill="none" stroke="#e95c59" stroke-width="9"/><path d="M22 78a28 28 0 0 1 56 0" fill="none" stroke="#f3b34a" stroke-width="9"/><path d="M32 78a18 18 0 0 1 36 0" fill="none" stroke="#58ad79" stroke-width="9"/><path d="M42 78a8 8 0 0 1 16 0" fill="none" stroke="#5598d1" stroke-width="9"/>';
 case "house":return '<path d="M12 46 50 14l38 32" fill="#df8e62" stroke="#995e43" stroke-width="4" stroke-linejoin="round"/><rect x="21" y="44" width="58" height="40" rx="3" fill="#f2d39b" stroke="#9a744a" stroke-width="3"/><rect x="43" y="58" width="16" height="26" fill="#9b7051" stroke="#72503b" stroke-width="2"/><rect x="27" y="53" width="12" height="12" fill="#a8d9ec" stroke="#6d9eb5" stroke-width="2"/><rect x="63" y="53" width="12" height="12" fill="#a8d9ec" stroke="#6d9eb5" stroke-width="2"/>';
 case "bread":return '<path d="M15 67q-7-34 19-42 17-18 37 0 22 7 15 42-2 13-16 13H31q-14 0-16-13Z" fill="#d9a45d" stroke="#9c6a36" stroke-width="3"/><path d="M33 35q-7 8-2 17m18-21q-7 9-2 18m18-15q-7 8-2 16" stroke="#f7d49b" stroke-width="5" stroke-linecap="round"/>';
 case "leg":return '<path d="M37 12h29l-3 35 12 28q3 10-8 11H42q-12 0-9-11l8-28Z" fill="#e7b58f" stroke="#a87558" stroke-width="3"/><path d="M39 48h24" stroke="#a87558" stroke-width="3"/><path d="M38 84h39" stroke="#a87558" stroke-width="5" stroke-linecap="round"/>';
 case "chair":return '<path d="M29 18h42v39H29Z" fill="#b87949" stroke="#80502f" stroke-width="3"/><path d="M22 49h56v12H22Z" fill="#d49a62" stroke="#80502f" stroke-width="3"/><path d="M28 61v25m44-25v25" stroke="#80502f" stroke-width="6" stroke-linecap="round"/>';
 case "cup":return '<path d="M20 24h50l-5 55H27Z" fill="#b7e2ee" stroke="#5c9eb7" stroke-width="4"/><path d="M70 34h9q14 0 9 16-3 11-20 10" fill="none" stroke="#5c9eb7" stroke-width="6"/><path d="M29 43h34" stroke="#fff" stroke-width="4" opacity=".8"/>';
 case "shadow":return '<circle cx="22" cy="22" r="12" fill="#ffd45b" stroke="#d6a33d" stroke-width="3"/><path d="M22 4v-4m0 44v-4M4 22H0m44 0h-4M9 9 5 5m34 34-4-4" stroke="#d6a33d" stroke-width="3"/><rect x="45" y="27" width="18" height="39" rx="4" fill="#7c9db4" stroke="#4f6c83" stroke-width="3"/><path d="M63 66 92 82H43Z" fill="#9aa3ad" opacity=".65"/><path d="M44 82h48" stroke="#697787" stroke-width="3"/>';
 case "block":return '<path d="m50 10 34 18v42L50 88 16 70V28Z" fill="#d9a65c" stroke="#956b36" stroke-width="3"/><path d="m16 28 34 19 34-19M50 47v41" fill="none" stroke="#956b36" stroke-width="3"/><path d="m25 33 25 14 25-14" fill="none" stroke="#f5d99e" stroke-width="3"/>';
 case "ribbon":return '<path d="M48 46 15 18q-8-7-10 5l8 24 35 8Zm4 0 33-28q8-7 10 5l-8 24-35 8Z" fill="#e77d9b" stroke="#a64f6c" stroke-width="3"/><circle cx="50" cy="49" r="12" fill="#f4a9bd" stroke="#a64f6c" stroke-width="3"/><path d="m43 60-9 28 17-10 17 10-9-28" fill="#e77d9b" stroke="#a64f6c" stroke-width="3"/>';
 case "rope":return '<path d="M17 70c-8-16 6-39 23-30s-4 25 12 27 23-12 12-21-26-4-23 8 25 15 37 2" fill="none" stroke="#c69a5b" stroke-width="12" stroke-linecap="round"/><path d="M17 70c-8-16 6-39 23-30s-4 25 12 27 23-12 12-21-26-4-23 8 25 15 37 2" fill="none" stroke="#8e673d" stroke-width="2" stroke-dasharray="4 4"/>';
 case "shell":return '<path d="M14 73q0-49 36-58 36 9 36 58Z" fill="#e8a8a0" stroke="#a96e72" stroke-width="3"/><path d="M50 16v57M50 20 27 72m23-52 23 52M30 33 18 69m52-36 12 36" stroke="#f7d2c9" stroke-width="4" fill="none"/><path d="M14 73h72" stroke="#a96e72" stroke-width="4"/>';
 case "soil":return '<path d="M10 53q18-10 40 0 20-12 40 0v33H10Z" fill="#a7784d" stroke="#795132" stroke-width="3"/><path d="M50 53V17m0 10q-17-17-27-4 10 18 27 10m0-4q17-17 27-4-10 18-27 10" fill="#78b76d" stroke="#4e8d4e" stroke-width="3"/><path d="M50 54 37 70m13-16 13 16m-13-9v17" stroke="#f1d29b" stroke-width="3" fill="none"/>';
 case "planet":return '<circle cx="50" cy="48" r="38" fill="#4d9fd3" stroke="#286d9c" stroke-width="4"/><path d="M24 30q12-15 23-7l-3 14-13 7-8-4Zm28 11 18-6 12 10-6 17-17 8-9-13Zm-30 9 11 5 5 14-10 7-9-14Z" fill="#6dbb70" stroke="#418b56" stroke-width="2"/><path d="M14 48q36-29 72 0" fill="none" stroke="#d8f2ff" stroke-width="2" opacity=".7"/>';
 case "rocket":return '<path d="M50 9q27 19 18 53H32Q23 28 50 9Z" fill="#dcecf5" stroke="#5c8095" stroke-width="3"/><circle cx="50" cy="39" r="9" fill="#78b9d8" stroke="#4c839e" stroke-width="3"/><path d="m32 49-16 17 18-2m34-15 16 17-18-2" fill="#e47d61" stroke="#a95543" stroke-width="3"/><path d="M40 63q-6 12 0 22l10-8 10 8q6-10 0-22" fill="#f2b54f" stroke="#d18a31" stroke-width="3"/>';
 case "shape":return '<circle cx="22" cy="48" r="13" fill="#62b6dc" stroke="#3e7f9b" stroke-width="3"/><rect x="43" y="35" width="25" height="25" rx="2" fill="#e5a85c" stroke="#a97636" stroke-width="3"/><path d="m83 61-13-26h26Z" fill="#86bd78" stroke="#4e8b4d" stroke-width="3"/>';
 case "table":return '<rect x="14" y="29" width="72" height="13" rx="4" fill="#c88b52" stroke="#83582f" stroke-width="3"/><path d="M23 42v42m54-42v42" stroke="#83582f" stroke-width="6" stroke-linecap="round"/>';
 case "door":return '<rect x="24" y="10" width="52" height="76" rx="3" fill="#c78b57" stroke="#805535" stroke-width="4"/><path d="M34 20h32v56H34Z" fill="#e2b27b" stroke="#9b6a40" stroke-width="2"/><circle cx="64" cy="49" r="3" fill="#74502e"/>';
 case "cupboard":return '<rect x="16" y="10" width="68" height="76" rx="5" fill="#d4a16b" stroke="#91633b" stroke-width="3"/><path d="M50 12v72M23 20h20v25H23Zm34 0h20v25H57Zm-34 32h20v27H23Zm34 0h20v27H57Z" fill="#f2d7ad" stroke="#a87848" stroke-width="2"/><circle cx="45" cy="58" r="2.5" fill="#80552f"/><circle cx="55" cy="58" r="2.5" fill="#80552f"/>';
 case "pollen":return '<circle cx="50" cy="48" r="10" fill="#f1bf4b" stroke="#c58f2d" stroke-width="3"/><g fill="#f3d57c" stroke="#c9a34b" stroke-width="2"><ellipse cx="50" cy="20" rx="9" ry="15"/><ellipse cx="50" cy="76" rx="9" ry="15"/><ellipse cx="22" cy="48" rx="15" ry="9"/><ellipse cx="78" cy="48" rx="15" ry="9"/></g><g fill="#d8a53f"><circle cx="13" cy="15" r="3"/><circle cx="86" cy="17" r="3"/><circle cx="12" cy="82" r="3"/><circle cx="88" cy="81" r="3"/></g>';
 case "waterdrop":return '<path d="M50 8C38 29 20 48 20 65a30 30 0 0 0 60 0C80 48 62 29 50 8Z" fill="#69c5e8" stroke="#358db4" stroke-width="4"/><path d="M34 65q2 14 15 16" stroke="#e7f8ff" stroke-width="5" fill="none" stroke-linecap="round"/>';
 case "smell":return '<path d="M38 15q-5 17-3 32l-8 13q-4 9 8 11h19q11-1 8-10l-8-12q-3-20-7-34" fill="#e8b69c" stroke="#a87561" stroke-width="3"/><path d="M67 22q12-7 15 4m-12 12q14-5 17 8" stroke="#75a8be" stroke-width="4" fill="none" stroke-linecap="round"/><circle cx="46" cy="62" r="2" fill="#7b4d42"/>';
 case "touch":return '<path d="M33 80 20 58q-5-9 3-13 7-3 11 5l-7-25q-2-10 7-12 9-1 10 9l4 19-1-29q0-10 9-10 9 1 8 11l-1 27 5-18q3-9 11-6 8 3 4 13l-8 28q-3 17-19 23Z" fill="#e9b99d" stroke="#a9765b" stroke-width="3"/>';
 case "pond":return '<path d="M9 55q40-24 82 0v27H9Z" fill="#7dc9e3" stroke="#418eae" stroke-width="3"/><path d="M9 57q20 9 40 0 20 9 42 0" fill="none" stroke="#d8f7ff" stroke-width="3"/><path d="M22 55V25m0 18q-12-15-17-5m17-2q12-15 17-5m38 24V21m0 25q-12-14-17-4m17-2q12-14 17-4" stroke="#5e9e60" stroke-width="4" fill="none" stroke-linecap="round"/><ellipse cx="55" cy="47" rx="12" ry="5" fill="#63a75c"/>';
 case "road":return '<path d="M29 8h42l20 80H9Z" fill="#697888" stroke="#435464" stroke-width="3"/><path d="M50 14v12m0 10v12m0 10v12m0 10v6" stroke="#fff3c5" stroke-width="5" stroke-linecap="round"/>';
 case "bicycle":return '<circle cx="23" cy="68" r="17" fill="none" stroke="#4d91b4" stroke-width="4"/><circle cx="77" cy="68" r="17" fill="none" stroke="#4d91b4" stroke-width="4"/><path d="m23 68 20-29 16 29H23l27-18 27 18M43 39h16m-16 0-8-8m42 37 9-28" fill="none" stroke="#d27e4e" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><circle cx="43" cy="39" r="4" fill="#e6b84d"/>';
 case "color":return '<path d="M50 12a38 38 0 1 0 0 76h8q8 0 5-8-4-8 4-10 18-4 18-21A37 37 0 0 0 50 12Z" fill="#f4e6ca" stroke="#9d825a" stroke-width="3"/><circle cx="32" cy="34" r="7" fill="#e85c5c"/><circle cx="51" cy="26" r="7" fill="#e7b94f"/><circle cx="69" cy="37" r="7" fill="#62b6dc"/><circle cx="34" cy="57" r="7" fill="#78b76d"/>';
 case "xylophone":return '<g stroke="#6c7e8d" stroke-width="2"><rect x="12" y="30" width="14" height="46" rx="3" fill="#e85d5d"/><rect x="28" y="24" width="14" height="52" rx="3" fill="#e7ad4f"/><rect x="44" y="18" width="14" height="58" rx="3" fill="#7cba78"/><rect x="60" y="24" width="14" height="52" rx="3" fill="#65b2d6"/><rect x="76" y="30" width="12" height="46" rx="3" fill="#a98bc7"/></g><path d="m25 12 18 9m32-10-18 9" stroke="#9b7048" stroke-width="4" stroke-linecap="round"/>';
 case "yoyo":return '<path d="M50 12v30" stroke="#8b6a4d" stroke-width="3"/><circle cx="50" cy="59" r="21" fill="#e87c63" stroke="#a74f40" stroke-width="4"/><circle cx="50" cy="59" r="7" fill="#f6c45d" stroke="#a74f40" stroke-width="3"/><path d="M50 38q14 12 0 21" stroke="#fff1c8" stroke-width="3" fill="none"/>';
 case "calendar":return '<rect x="12" y="17" width="76" height="66" rx="8" fill="#f4f8fc" stroke="#6d8da5" stroke-width="3"/><rect x="12" y="17" width="76" height="18" rx="6" fill="#6ca7c8"/><path d="M31 10v17m38-17v17" stroke="#6d8da5" stroke-width="5" stroke-linecap="round"/><g fill="#e6a94c"><rect x="24" y="44" width="13" height="13" rx="3"/><rect x="44" y="44" width="13" height="13" rx="3"/><rect x="64" y="44" width="13" height="13" rx="3"/></g><text x="50" y="76" text-anchor="middle" font-size="16" font-weight="900" fill="#4c6d83">M T W</text>';


 case "fraction":return '<rect x="12" y="24" width="76" height="48" rx="5" fill="#f7f8fb" stroke="#617c96" stroke-width="3"/><path d="M37 24v48M63 24v48" stroke="#617c96" stroke-width="3"/><rect x="13" y="25" width="23" height="46" fill="#62a9d2"/>';
 case "piaster":case "coin":return '<circle cx="50" cy="48" r="36" fill="#f1c65e" stroke="#b98a2f" stroke-width="4"/><circle cx="50" cy="48" r="27" fill="none" stroke="#fff0b7" stroke-width="3"/><text x="50" y="56" text-anchor="middle" font-size="20" font-weight="900" fill="#815b23">100</text>';
 case "pound":return '<rect x="9" y="24" width="82" height="48" rx="6" fill="#83c69a" stroke="#3d8b65" stroke-width="4"/><rect x="16" y="30" width="68" height="36" rx="4" fill="none" stroke="#d9f2df" stroke-width="2"/><text x="50" y="57" text-anchor="middle" font-size="25" font-weight="900" fill="#286747">£</text>';
 case "temperature":case "thermometer":return '<rect x="42" y="10" width="16" height="58" rx="8" fill="#f8fbfd" stroke="#67859a" stroke-width="4"/><circle cx="50" cy="75" r="17" fill="#e76f62" stroke="#a94743" stroke-width="4"/><path d="M50 35v39" stroke="#e76f62" stroke-width="8" stroke-linecap="round"/><path d="M62 25h8m-8 15h8m-8 15h8" stroke="#67859a" stroke-width="3" stroke-linecap="round"/>';


 case "weight":case "kilogram":case "gram":case "scale":return '<path d="M50 16v58M24 29h52M50 16l-9 10h18Z" fill="none" stroke="#5d7185" stroke-width="5" stroke-linecap="round" stroke-linejoin="round"/><path d="M24 29 12 59h24ZM76 29 64 59h24Z" fill="#e8f1f8" stroke="#64849b" stroke-width="3" stroke-linejoin="round"/><path d="M12 59q12 14 24 0M64 59q12 14 24 0" fill="none" stroke="#64849b" stroke-width="3"/><path d="M35 77h30" stroke="#5d7185" stroke-width="5" stroke-linecap="round"/>';

 case "generic":default:return '<rect x="15" y="13" width="70" height="64" rx="14" fill="#e6f0f8" stroke="#8aa9c1" stroke-width="3"/><path d="M27 60 43 40l12 12 9-13 12 21Z" fill="#80bf92" stroke="#4b8a60" stroke-width="2"/><circle cx="65" cy="29" r="7" fill="#ffd45b"/>';
 }
}
function svg(kind,label){
 var k=kind||"generic",b=body(k),value=String(label||"");
 if(k==="letter"&&/^[A-Za-z]$/.test(value))b='<rect x="15" y="12" width="70" height="68" rx="15" fill="#fff2d4" stroke="#d8b05b" stroke-width="3"/><text x="50" y="64" text-anchor="middle" font-size="48" font-weight="900" fill="#9b7024">'+esc(value.toUpperCase())+'</text>';
 if(k==="number"&&/^\d{1,3}$/.test(value))b='<rect x="15" y="12" width="70" height="68" rx="15" fill="#e7f2fb" stroke="#86a9c2" stroke-width="3"/><text x="50" y="62" text-anchor="middle" font-size="39" font-weight="900" fill="#356d99">'+esc(value)+'</text>';
 return '<svg class="visual-art" data-visual-icon-kind="'+esc(k)+'" viewBox="0 0 100 96" role="img" aria-label="'+esc(label||k)+'" focusable="false"><title>'+esc(label||k)+'</title>'+b+'</svg>';
}
function hasSpecificVisual(kind){return !!kind&&kind!=="generic"&&body(kind)!==body("generic");}
function card(kind,label,extra){
 return '<div class="visual-object-card">'+svg(kind,label)+'<span>'+esc(label||"")+'</span>'+(extra||"")+'</div>';
}
function wrap(title,content,kind){
 var visualKind=kind||"concept";
 if(content.indexOf('<svg')<0){var artKind=/number|pattern|line|place-value|ten-frame|bar-model/i.test(visualKind)?"number":/money|coin/i.test(visualKind)?"coin":/clock/i.test(visualKind)?"clock":/story|sentence|reading/i.test(visualKind)?"book":"generic";content='<div class="visual-engine-symbol">'+svg(artKind,artKind)+'</div>'+content;}
 return '<div class="visual-stage visual-engine-scene" data-visual-engine="semantic-svg" data-visual-kind="'+esc(visualKind)+'"><div class="visual-caption">'+esc(title||"شاهد الصورة وفكّر")+'</div>'+content+'</div>';
}
function objectGrid(kind,n,label){
 var count=Math.max(1,Math.min(30,Number(n)||1)),items=[];
 for(var i=0;i<count;i++)items.push('<span class="visual-object">'+svg(kind,label||kind)+'</span>');
 return '<div class="visual-object-grid" aria-label="'+esc(count+" "+(label||kind))+'">'+items.join("")+'</div>';
}
function getQuestionText(q,d){
 return [q.prompt,d.title,d.question,d.passage,d.story].filter(Boolean).join(" ");
}
function optionKind(value){var s=String(value||"").trim();if(/^[A-Za-z]$/.test(s))return "letter";if(/^\d+$/.test(s))return "number";return kindOf(value)||"generic";}
function sceneFromText(text,subject){
 var kinds=sceneKinds(text,4);
 if(!kinds.length){
  if(/math/i.test(subject||""))kinds=["number","block","star"];
  else if(/science/i.test(subject||""))kinds=["plant","sun","water"];
  else kinds=["book","letter","pencil"];
 }
 return '<div class="visual-story-scene"><div class="visual-story-sky"><span class="visual-sun-dot"></span></div><div class="visual-story-items">'+kinds.map(function(k){return '<span>'+svg(k,k)+'</span>';}).join("")+'</div><div class="visual-story-ground"></div></div>';
}
function numberCards(values,missingIndex){
 return '<div class="visual-number-cards">'+values.map(function(n,i){return '<span class="'+(i===missingIndex?"is-missing":"")+'">'+(n===null?"?":esc(n))+'</span>';}).join("")+'</div>';
}
function countQuestion(q,d,text){
 var n=numOf(d.count);if(n===null)n=numbersIn(q.prompt||d.title)[0]?.value;
 if(n===null||n===undefined)n=3;
 n=Math.max(1,Math.min(30,Number(n)||3));
 var kind=kindOf(d.item||"")!=="generic"?kindOf(d.item):kindOf(text);
 if(kind==="generic")kind="star";
 var items=[];for(var i=1;i<=n;i++)items.push('<button type="button" class="count-tap-item" data-count-step="'+i+'" aria-label="اضغط لعدّ العنصر رقم '+i+'">'+svg(kind,kind)+'<span class="count-tap-number">'+i+'</span></button>');
 return wrap("اضغط على كل صورة وعدّ معها",'<div class="visual-object-grid count-interactive-grid">'+items.join("")+'</div><div class="count-progress" aria-live="polite"><span>اضغط على الصور واحدةً واحدة</span><b data-count-total>0 / '+n+'</b></div>', "count");
}
function groupedArray(q,d,text){
 var v=d.visual||{},rows=Number(v.rows||v.groups||0),cols=Number(v.cols||v.each||0);
 if(!rows||!cols)return "";
 rows=Math.max(1,Math.min(8,rows));cols=Math.max(1,Math.min(10,cols));
 var prompt=String(q.prompt||d.title||text),phrase=/\b(\d+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s+(plates?|groups?)\s+(?:with|have)\s+(\d+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s+([a-z]+)\s+(?:on\s+each|each)\b/i.exec(prompt);
 var container=phrase?kindOf(phrase[2]):"group",item=phrase?kindOf(phrase[4]):kindOf(text+" "+(v.label||""));
 if(container==="generic")container="group";if(item==="generic")item="dot";
 var groups=[];
 for(var r=0;r<rows;r++)groups.push('<div class="visual-array-group"><div class="visual-container-art">'+svg(container,container)+'<b>'+(container==="plate"?"طبق ": "مجموعة ")+(r+1)+'</b></div>'+objectGrid(item,cols,item)+'</div>');
 return wrap("شاهد المجموعات: كل مجموعة فيها أشياء متساوية",'<div class="visual-array-groups">'+groups.join("")+'</div><div class="visual-array-total">'+rows+' مجموعات × '+cols+' في كل مجموعة</div>',"array");
}
function countedEntities(prompt){
 var source=String(prompt||""),token="(?:zero|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve|\\d+)",re=new RegExp("\\b("+token+")\\s+","gi"),matches=[],out=[],m;
 while((m=re.exec(source))!==null)matches.push({raw:m[0],value:m[1],index:m.index,start:re.lastIndex});
 var colors={red:1,blue:1,green:1,yellow:1,purple:1,white:1,black:1,brown:1,grey:1,gray:1,pink:1,orange:1};
 for(var i=0;i<matches.length;i++){
  var item=matches[i],end=i+1<matches.length?matches[i+1].index:source.length,tail=source.slice(item.start,end),words=(tail.match(/[a-z]+/gi)||[]),kind="generic",noun="",modifier="";
  for(var j=0;j<words.length;j++){
   var candidate=words[j],found=kindOf(candidate);
   if(colors[found]){modifier=found;continue;}
   if(found!=="generic"){kind=found;noun=candidate;break;}
  }
  var count=numOf(item.value);if(count===null||count<0||count>30)continue;
  out.push({count:count,kind:kind,noun:noun,modifier:modifier,raw:item.raw+noun,index:item.index});
 }
 var common=out.find(function(x){return x.kind!=="generic"&&!colors[x.kind]});
 if(common)out.forEach(function(x){if(x.kind==="generic"&&x.modifier){x.kind=common.kind;x.noun=common.noun||common.kind+"s"}});
 return out.filter(function(x){return x.kind!=="generic"});
}
function coloredObjectGrid(kind,n,color){
 var count=Math.max(1,Math.min(30,Number(n)||1)),palette={red:"#e64c4c",blue:"#428fd1",green:"#48a86b",yellow:"#efc342",purple:"#9864c8",orange:"#ed9340",pink:"#e783ad",black:"#384454",white:"#f4f5f7",brown:"#9b6b48",grey:"#929eaa",gray:"#929eaa"},fill=palette[color]||"#428fd1",items=[];
 if(kind!=="ball")return objectGrid(kind,count,kind);
 for(var i=0;i<count;i++)items.push('<span class="visual-object"><svg class="visual-art" viewBox="0 0 100 96" role="img" aria-label="'+esc(color+" ball")+'"><circle cx="50" cy="48" r="34" fill="'+fill+'" stroke="#42566b" stroke-width="3"/><path d="M18 42q30 10 64-2M36 16q-8 30 2 64m26-64q8 30-2 64" fill="none" stroke="#ffffff" stroke-opacity=".55" stroke-width="4"/></svg></span>');
 return '<div class="visual-object-grid">'+items.join("")+'</div>';
}
function entityGrid(entity){
 return entity.modifier?coloredObjectGrid(entity.kind,entity.count,entity.modifier):objectGrid(entity.kind,entity.count,entity.kind);
}
function mixedEntityProblem(q,d,text){
 var prompt=String(q.prompt||d.title||text||""),groups=countedEntities(prompt);
 if(groups.length<2)return "";
 var subtraction=/\b(minus|subtract|take away|left|remain|fewer|less|gave away|gives away|lost)\b/i.test(prompt);
 var addition=/\b(plus|add|altogether|in all|total|together|combined)\b/i.test(prompt)||/\b(and)\b/i.test(prompt);
 if(!subtraction&&!addition)return "";
 var a=groups[0],b=groups[1],op=subtraction?"−":"+";
 return wrap("شاهد كل مجموعة بعناصرها ثم افهم العملية",'<div class="visual-operation-groups visual-mixed-entities"><div class="visual-math-group"><b>المجموعة الأولى · '+esc(a.noun)+'</b>'+entityGrid(a)+'<strong>'+a.count+'</strong></div><div class="visual-operation-sign">'+op+'</div><div class="visual-math-group"><b>المجموعة الثانية · '+esc(b.noun)+'</b>'+entityGrid(b)+'<strong>'+b.count+'</strong></div></div><div class="visual-operation-unknown"><span>المجموع / المتبقي</span><b>؟</b></div>',"mixed-entity-operation");
}
function wordProblem(q,d,text){
 var v=d.visual||{},prompt=String(q.prompt||d.title||""),nums=numbersIn(prompt);
 var noun=/\b(?:has|have|are|is)\s+(?:a\s+)?(?:\d+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s+([a-z]+)/i.exec(prompt);
 var kinds=sceneKinds(text,5),kind=noun?kindOf(noun[1]):(kinds[0]||"block");
 if(kind==="generic")kind="dot";
 if(v.kind==="array")return groupedArray(q,d,text);
 var mixed=mixedEntityProblem(q,d,text);if(mixed)return mixed;
 if(/by the pond|on the grass|on the ground/i.test(prompt)&&nums.length>=2){
  var totalBirds=Math.min(30,nums[0].value),known=Math.min(totalBirds,nums[1].value);
  var birds=Array.from({length:totalBirds}).map(function(_,i){return '<span class="'+(i<known?"known":"unknown")+'">'+svg("bird","bird")+'</span>';}).join("");
  return wrap("المجموعة الكاملة والجزء المعروف",'<div class="visual-bird-groups"><b>الكل: '+totalBirds+'</b><div>'+birds+'</div><p>بجانب البركة: '+known+' · على العشب: ؟</p></div>',"missing-part");
 }
 if(v.kind==="bar-model"){
  var total=Number(v.total)||nums[0]?.value||0,part=Number(v.part)||nums[1]?.value||0;
  return wrap("شاهد الكل والجزء المعروف",'<div class="visual-bar-model"><div><b>الكل</b><strong>'+esc(total)+'</strong><i style="width:100%"></i></div><div><b>الجزء المعروف</b><strong>'+esc(part)+'</strong><i style="width:'+Math.max(5,Math.min(100,part/Math.max(1,total)*100))+'%"></i></div><div class="visual-bar-missing"><b>الجزء المتبقي</b><strong>؟</strong></div></div>',"bar-model");
 }
 var groupScene=groupsFromPrompt(q,text);if(groupScene)return groupScene;
 if(/now\s+(?:she|he|it|they|[a-z]+)\s+has\b/i.test(prompt)&&nums.length>=2){
  return wrap("من العدد الأول إلى المجموع النهائي",'<div class="visual-change-line"><div>'+card(kind,kind)+'<b>'+nums[0].value+'</b></div><span>＋ ؟</span><span>→</span><div>'+card(kind,kind)+'<b>'+nums[1].value+'</b></div></div>',"missing-part");
 }
 if(/cm\b|centimet/i.test(prompt)&&nums.length>=2){
  var measures=[...prompt.matchAll(/\b(?:a\s+)?([a-z]+)\s+(?:is|measures)\s+(\d+)\s*cm/gi)];
  var firstName=measures[0]?measures[0][1]:"الطول الأول",firstValue=measures[0]?Number(measures[0][2]):nums[0].value;
  var secondName=measures[1]?measures[1][1]:"الطول الثاني",secondValue=measures[1]?Number(measures[1][2]):nums[1].value;
  var max=Math.max(firstValue,secondValue),difference=/longer|shorter|difference|how much more|how much less/i.test(prompt);
  return wrap(difference?"اكتشف الفرق بين الطولين":"قارن بين الطولين",'<div class="visual-measure-bars"><div><b>'+esc(firstName)+' · '+firstValue+' cm</b><i style="width:'+firstValue/max*100+'%"></i></div><div><b>'+esc(secondName)+' · '+secondValue+' cm</b><i style="width:'+secondValue/max*100+'%"></i></div></div>'+(difference?'<div class="visual-measure-difference"><span>'+firstValue+' cm</span><b>−</b><span>'+secondValue+' cm</span><b>= ؟</b></div>':"")+svg("ruler","مسطرة"),"measurement");
 }
 var phrase=/\b(\d+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s+([a-z]+)\s+with\s+(\d+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s+([a-z]+)\s+(?:on\s+each|each)\b/i.exec(prompt);
 if(phrase){
  var rows=numOf(phrase[1]),each=numOf(phrase[3]),container=kindOf(phrase[2]),item=kindOf(phrase[4]);
  if(container==="generic")container="plate";if(item==="generic")item=kind;
  if(rows>0&&rows<=8&&each>0&&each<=8){
   var cards=[];for(var i=0;i<rows;i++)cards.push('<div class="visual-array-group"><div class="visual-container-art">'+svg(container,container)+'<b>'+esc(phrase[2])+' '+(i+1)+'</b></div>'+objectGrid(item,each,phrase[4])+'</div>');
   return wrap("كل مجموعة فيها نفس العدد",'<div class="visual-array-groups">'+cards.join("")+'</div><div class="visual-array-total">'+rows+' مجموعات · '+each+' في كل مجموعة</div>',"equal-groups");
  }
 }
 if(nums.length>=2){
  var a=nums[0].value,b=nums[1].value;
  if(a>0&&b>0&&a<=30&&b<=30){
   var op=/\b(minus|subtract|left|remain|fewer|less|difference|take away)\b/i.test(prompt)?"−":/\b(more|plus|add|altogether|total|gives|gets|together)\b/i.test(prompt)?"+":"·";
   return wrap("قسّم المسألة إلى مجموعتين بصريتين",'<div class="visual-operation-groups"><div class="visual-math-group"><b>المجموعة الأولى</b>'+objectGrid(kind,a,kind)+'<strong>'+a+'</strong></div><div class="visual-operation-sign">'+op+'</div><div class="visual-math-group"><b>المجموعة الثانية</b>'+objectGrid(kind,b,kind)+'<strong>'+b+'</strong></div></div>',"word-problem");
  }
 }
 if(nums.length)return wrap("شاهد الأعداد والأشياء",'<div class="visual-concept-row">'+card(kind,kind)+numberCards(nums.slice(0,4).map(function(x){return x.value;}))+'</div>',"word-problem");
 return wrap("تخيّل الموقف في الصورة",sceneFromText(text,q.skill),"word-problem");
}
function placeValue(q,d,text){
 var m=/(\d+)\s*Hundreds?,\s*(\d+)\s*Tens?,\s*(?:and\s*)?(\d+)\s*Ones?/i.exec(q.prompt||d.title||"");
 if(!m)return "";
 var h=Number(m[1]),t=Number(m[2]),o=Number(m[3]);
 return wrap("المئات والعشرات والآحاد",svg("block","مكعبات القيمة المكانية")+'<div class="visual-place-groups"><div><b>مئات</b><div class="visual-hundred-blocks">'+Array.from({length:Math.min(h,9)}).map(()=>'<span class="visual-hundred-block"></span>').join("")+'</div><strong>'+h+'</strong></div><div><b>عشرات</b><div class="visual-ten-rods">'+Array.from({length:Math.min(t,9)}).map(()=>'<span></span>').join("")+'</div><strong>'+t+'</strong></div><div><b>آحاد</b><div class="visual-unit-cubes">'+Array.from({length:Math.min(o,9)}).map(()=>'<span></span>').join("")+'</div><strong>'+o+'</strong></div></div>',"place-value");
}
function numberPattern(q,text){
 var nums=numbersIn(q.prompt||"").map(function(x){return x.value;}).slice(0,6);
 if(!nums.length)return colorPattern(q,text)||"";
 var cells=nums.concat([null]).map(function(n){
  if(n===null)return '<div class="visual-pattern-cell is-missing"><b>؟</b><span class="visual-pattern-dots">؟</span></div>';
  return '<div class="visual-pattern-cell"><b>'+esc(n)+'</b><span class="visual-pattern-dots">'+Array.from({length:Math.min(10,n)}).map(function(){return '<i></i>';}).join("")+'</span></div>';
 });
 return wrap("تابع تسلسل الأعداد وعدّ النقاط",'<div class="visual-number-pattern">'+cells.join("")+'</div>',"number-pattern");
}
function colorSequence(text){
 var colors=[],keys=Object.keys(EMOJI);
 for(var ch of String(text||"")){if(EMOJI[ch]&&["red","blue","green","yellow","purple","black","white","brown","grey"].indexOf(EMOJI[ch])>=0)colors.push(EMOJI[ch]);}
 return colors;
}
function colorRow(colors){
 return '<div class="visual-color-row">'+colors.map(function(k){return '<span>'+svg(k,k)+'</span>';}).join("")+'</div>';
}
function colorPattern(q,text){
 var prompt=String(q.prompt||text||""),parts=/Picture B:/i.test(prompt)?prompt.split(/Picture B:/i):[prompt],a=colorSequence(parts[0]),b=parts[1]?colorSequence(parts[1]):[];
 if(!a.length)return "";
 var content='<div class="visual-pattern-row"><b>'+(b.length?"الصورة A":"النمط")+'</b>'+colorRow(a)+'</div>';
 if(b.length)content+='<div class="visual-pattern-row"><b>الصورة B</b>'+colorRow(b)+'</div>';
 else content+='<div class="visual-pattern-row"><b>التالي</b><span class="visual-color-missing">؟</span></div>';
 return wrap("لاحظ ترتيب الألوان وقارن بينها",content,"color-pattern");
}
function rulerVisual(q,d){
 var ticks=Math.max(5,Math.min(12,Number((d.visual||{}).ticks)||8)),length=Number((d.visual||{}).length)||6;
 return wrap("المسطرة: ابدأ من الصفر وعدّ المسافات",svg("ruler","مسطرة")+'<div class="visual-ruler"><div class="visual-ruler-bar">'+Array.from({length:ticks}).map(function(_,i){return '<span><i></i><b>'+i+'</b></span>';}).join("")+'</div><div class="visual-ruler-line" style="width:'+Math.max(20,Math.min(100,length/(ticks-1)*100))+'%"></div></div>',"ruler");
}
function groupsFromPrompt(q,text){
 var prompt=String(q.prompt||text||""),m=/\b(one|two|three|four|five|six|seven|eight|nine|ten|\d+)\s+groups?\s+(?:of|have)\s+(one|two|three|four|five|six|seven|eight|nine|ten|\d+)(?:\s+([a-z]+))?/i.exec(prompt);
 if(!m)return "";
 var rows=numOf(m[1]),cols=numOf(m[2]),kind=kindOf(m[3]||"counter");if(kind==="generic")kind="dot";
 if(!rows||!cols||rows>8||cols>10)return "";
 var groups=[];for(var i=0;i<rows;i++)groups.push('<div class="visual-array-group"><b>مجموعة '+(i+1)+'</b>'+objectGrid(kind,cols,kind)+'</div>');
 return wrap("انظر إلى المجموعات المتساوية",'<div class="visual-array-groups">'+groups.join("")+'</div>',"equal-groups");
}
function mathConcept(q,d,text){
 var prompt=String(q.prompt||""),nums=numbersIn(prompt);
 if(/come after|after/i.test(prompt)&&nums.length>=2){
  var first=nums[0].value,second=nums[1].value;
  return wrap("هل العدد يأتي بعد العدد الآخر؟",'<div class="visual-number-order"><div>'+card("number",String(second))+'</div><span>←</span><div>'+card("number",String(first))+'</div></div><div class="visual-order-caption">تحرّك إلى الأمام على خط الأعداد</div>',"number-order");
 }
 if(/needs?\s+two\s+more\s+to\s+make\s+ten|make\s+ten/i.test(prompt)&&nums.length){
  var filled=Math.min(10,nums[0].value);return wrap("إطار العشرة: الممتلئ والفارغ",'<div class="visual-ten-frame">'+Array.from({length:10}).map(function(_,i){return '<span class="'+(i<filled?"filled":"")+'"></span>';}).join("")+'</div>',"ten-frame");
 }
 if(/one ten|tens? and .*ones?/i.test(prompt)){
  var n=nums[0]?nums[0].value:16,t=Math.floor(n/10),o=n%10;
  return wrap("العشرات والآحاد",'<div class="visual-place-groups"><div><b>عشرات</b><div class="visual-ten-rods">'+Array.from({length:Math.min(t,9)}).map(function(){return '<span></span>';}).join("")+'</div><strong>'+t+'</strong></div><div><b>آحاد</b><div class="visual-unit-cubes">'+Array.from({length:Math.min(o,9)}).map(function(){return '<span></span>';}).join("")+'</div><strong>'+o+'</strong></div></div>',"place-value");
 }
 if(/ruler|measur/i.test(prompt))return rulerVisual(q,d);
 if(/bar model/i.test(prompt))return wrap("نموذج الشريط: كلّ وأجزاء",'<div class="visual-bar-model"><div class="visual-bar-whole">الكل</div><div class="visual-bar-parts"><span>جزء</span><span>جزء</span></div></div>',"bar-model");
 return groupsFromPrompt(q,text)||"";
}
function graph(q,text){
 var matches=[...String(q.prompt||"").matchAll(/([A-Za-z]+)\s*=\s*(\d+)/g)].slice(0,4);
 if(!matches.length)return "";
 var rows=matches.map(function(m){var kind=kindOf(m[1]);if(kind==="generic")kind="block";var n=Number(m[2]);return '<div class="visual-graph-row"><b>'+esc(m[1])+'</b><div>'+objectGrid(kind,Math.min(n,12),m[1])+'</div><strong>'+n+'</strong></div>';});
 return wrap("قارن بين المجموعات في الرسم",'<div class="visual-graph">'+rows.join("")+'</div>',"graph");
}
function money(q,d){
 var coins=Array.isArray(d.coins)?d.coins:[1,5,10],target=d.target||numbersIn(q.prompt)[0]?.value||0;
 return wrap("النقود والعملات",'<div class="visual-coins">'+coins.slice(0,8).map(function(n){return '<div class="visual-coin">'+svg("coin","coin")+'<b>'+esc(n)+'¢</b></div>';}).join("")+'</div><div class="visual-money-target">المطلوب تكوينه: <b>'+esc(target)+'¢</b></div>',"money");
}
function matchPairs(q,d){
 var pairs=Array.isArray(d.pairs)?d.pairs:[];
 if(!pairs.length)return "";
 return wrap("انظر إلى الصور والكلمات",'<div class="visual-pairs">'+pairs.slice(0,6).map(function(p){var a=String(p.a||""),b=String(p.b||"");return '<div class="visual-pair">'+card(kindOf(a),a)+'<span class="visual-pair-link">↔</span><b>'+esc(b)+'</b></div>';}).join("")+'</div>',"matching");
}
function reading(q,d,text){
 var story=d.story||d.passage||q.prompt||text;
 return wrap("مشهد من القصة",sceneFromText(story,q.skill)+'<div class="visual-story-strip">'+sceneKinds(story,4).map(function(k){return '<span>'+esc(k)+'</span>';}).join(" · ")+'</div>',"story");
}
function optionCards(options,q){
 if(!Array.isArray(options)||!options.length)return "";
 return '<div class="visual-option-gallery">'+options.slice(0,4).map(function(o){var label=String(o).replace(/[\u{1F300}-\u{1FAFF}\u{2600}-\u{27BF}]/gu,"").trim()||String(o),visual=renderOption(q||{},label),truthLabel=q&&q.type==="true_false"?'<span class="visual-option-label">'+esc(label)+'</span>':"";return '<div class="visual-object-card">'+visual+truthLabel+'</div>';}).join("")+'</div>';
}
function sceneKinds(text,limit){var kinds=findKinds(text).filter(function(k){return k!=="number"&&k!=="letter"&&k!=="shape";});return kinds.slice(0,limit||4);}
var LETTER_WORDS={A:"apple",B:"ball",C:"cat",D:"dog",E:"egg",F:"fish",G:"green",H:"hat",I:"ice",J:"jug",K:"kite",L:"leaf",M:"moon",N:"nest",O:"orange",P:"pencil",Q:"person",R:"rock",S:"sun",T:"tree",U:"umbrella",V:"van",W:"whale",X:"xylophone",Y:"yoyo",Z:"zebra"};
function letterScene(letter,options,q){
 var l=String(letter||"A").toUpperCase(),kind=LETTER_WORDS[l]||"letter";
 var main=card("letter",l)+card(kind,kind);
 var extra=Array.isArray(options)&&options.length?'<div class="visual-prompt-options">'+optionCards(options,q)+'</div>':"";
 return wrap("الحرف وصورته المساعدة",'<div class="visual-letter-lesson">'+main+'</div>'+extra,"letter-sound");
}
function wordChoiceCards(options){
 return '<div class="visual-option-gallery">'+options.slice(0,4).map(function(o){
  var word=String(o),first=(word.match(/[A-Za-z]/)||["A"])[0].toUpperCase(),kind=LETTER_WORDS[first]||"letter";
  return '<div class="visual-object-card">'+svg(kind,kind)+'<span class="visual-word-label">'+esc(word)+'</span></div>';
 }).join("")+'</div>';
}
function scienceScene(q,d,text){
 var prompt=String(q.prompt||"").toLowerCase();
 if(/sense|ears|eyes|nose|tongue|hearing|smell|taste/i.test(prompt))return wrap("الحواس الخمس",'<div class="visual-senses">'+card("sight","sight")+card("ear","hearing")+card("smell","smell")+card("taste","taste")+card("touch","touch")+'</div>',"five-senses");
 if(/food chain/.test(prompt))return wrap("سلسلة الغذاء: تنتقل الطاقة بين الكائنات",'<div class="visual-food-chain">'+card("sun","الشمس")+'<span>→</span>'+card("plant","نبات")+'<span>→</span>'+card("rabbit","أرنب")+'<span>→</span>'+card("fox","ثعلب")+'</div>',"food-chain");
 if(/polar habitat|polar habitats|arctic habitat/i.test(prompt))return wrap("موطن الدب القطبي البارد",'<div class="visual-polar-scene">'+card("bear","دب قطبي")+card("ice","جليد")+'</div>',"polar-habitat");
 if(/balanced diet|nutrition/i.test(prompt))return wrap("الغذاء المتوازن",'<div class="visual-nutrition-scene">'+card("food","طبق غذاء متوازن")+card("apple","فاكهة")+card("carrot","خضروات")+card("milk","حليب")+'</div>',"nutrition");
 if(/lungs|breathing|breathe/i.test(prompt))return wrap("الرئتان تساعداننا على التنفس",card("lungs","الرئتان"),"breathing");
 if(/heat conductor|conductors|transfers heat/i.test(prompt))return wrap("المعدن ينقل الحرارة",'<div class="visual-heat-scene">'+card("metal","قطعة معدن")+'<span class="visual-heat-waves">♨</span></div>',"heat-conduction");
 return "";
}
function promptScene(q,d,text){
 var kinds=sceneKinds(text,4),opts=Array.isArray(q.options)?q.options:[];
 var listen=/listen and find:\s*([a-z0-9])/i.exec(String(q.prompt||""));
 if(listen&&/[A-Za-z]/.test(listen[1]))return letterScene(listen[1],opts,q);
 if(listen&&/\d/.test(listen[1]))return wrap("شاهد العدد وصورته",card("number",listen[1])+objectGrid("dot",Math.max(1,Math.min(10,Number(listen[1])||1)),"نقطة"),"number");
 if(kinds.length){
  var main=kinds.slice(0,3).map(function(k){return card(k,k);}).join("");
  var optHtml=opts.length&&opts.some(function(x){return optionKind(x)!=="generic";})?'<div class="visual-prompt-options">'+optionCards(opts,q)+'</div>':'';
  return wrap("صورة تساعدك على فهم السؤال",'<div class="visual-prompt-scene">'+main+'</div>'+optHtml,"prompt-scene");
 }
 if(opts.length&&opts.every(function(x){return /^[A-Z]$/i.test(String(x).trim())||/^\d+$/.test(String(x).trim());})){
  return wrap("شاهد الحروف أو الأعداد",optionCards(opts,q),"letters");
 }
 if(opts.length){
  var recognized=opts.some(function(x){return optionKind(x)!=="generic";});
  return wrap("صور تساعدك على فهم الكلمات",optionCards(opts,q),"options");
 }
 return "";
}

function quotedWordScene(q,d,text){
 var m=/[“"]([A-Za-z]+)[”"]/i.exec(String(q.prompt||""));
 if(!m)return "";
 var word=m[1],kind=kindOf(word);
 if(kind==="generic")return letterScene(word.charAt(0),[]);
 return wrap("صورة الكلمة التي تسمعها",card(kind,kind)+'<div class="visual-word-label">'+esc(word.toUpperCase())+'</div>',"phonics-word");
}
function genericVisual(q,d,text){
 var subject=String(q.skill||"");
 var base=/math/i.test(subject)?["number","block","star"]:/science/i.test(subject)?["plant","sun","water"]:["book","letter","pencil"];
 var options=Array.isArray(q.options)?q.options:[];
 var extras=options.length?optionCards(options,q):base.map(function(k){return card(k,k);}).join("");
 return wrap("شاهد الصورة ثم فكّر في السؤال",'<div class="visual-generic-scene">'+sceneFromText(text,subject)+'</div>'+extras,"concept");
}

function daysCapitalization(){
 var days=["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
 var cards=days.map(function(day){return '<div class="visual-day-card" style="background:#fff;border:2px solid #d9e6f1;border-radius:12px;padding:9px 12px;min-width:92px;text-align:center"><strong style="font-size:24px;color:#e18a35">'+esc(day.charAt(0))+'</strong><span style="font-size:15px;font-weight:800">'+esc(day.slice(1))+'</span></div>';}).join("");
 return wrap("أيام الأسبوع تبدأ بحرف كبير",svg("calendar","أيام الأسبوع")+'<div class="visual-days-capital" style="display:flex;flex-wrap:wrap;justify-content:center;gap:8px">'+cards+'</div><p style="text-align:center;font-weight:800;color:#4b6681">الحرف الأول كبير في اسم كل يوم</p>',"capitalization");
}
function mainIdeaSupport(){
 return wrap("الفكرة الرئيسية والتفاصيل الداعمة",'<div class="visual-main-idea" style="display:grid;grid-template-columns:1fr;gap:10px;text-align:center"><div style="background:#eaf2ff;border:2px solid #8eb4df;border-radius:16px;padding:12px">'+svg("book","كتاب")+'<b style="display:block">الفكرة الرئيسية</b><small>ما الموضوع الأساسي؟</small></div><div style="font-size:22px;color:#d59b35">↓</div><div style="display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:7px">'+["معلومة ١","معلومة ٢","مثال"].map(function(x){return '<div style="background:#fff4df;border:1px solid #edcf8b;border-radius:12px;padding:10px;font-weight:800">'+svg("star",x)+'<span style="display:block">'+x+'</span></div>';}).join("")+'</div><p style="margin:0;font-weight:800;color:#4b6681">التفاصيل تشرح الفكرة وتدعمها</p></div>',"main-idea-support");
}
function inferenceEvidence(){
 return wrap("نستخدم أدلة النص لنصل إلى استنتاج",'<div class="visual-inference" style="display:grid;grid-template-columns:1fr auto 1fr;gap:8px;align-items:center;text-align:center"><div style="background:#eaf7f1;border:2px solid #9acdb8;border-radius:15px;padding:10px">'+svg("book","صفحة النص")+'<b style="display:block">دليل من النص</b><small>كلمات وتفاصيل</small></div><div style="font-size:24px;font-weight:900;color:#d49a36">→</div><div style="background:#fff3df;border:2px solid #e9c98b;border-radius:15px;padding:10px">'+svg("brain","تفكير")+'<b style="display:block">استنتاج</b><small>ما الذي نفهمه من الدليل؟</small></div></div><p style="text-align:center;font-weight:800;color:#4b6681">الدليل + التفكير = استنتاج مدعوم</p>',"text-evidence-inference");
}



function measurementScene(q,d,text){
 var prompt=String(q.prompt||d.title||text||""),values=[...prompt.matchAll(/(\d+)\s*cm\b/gi)].map(function(x){return Number(x[1]);});
 if(values.length<2)return "";
 var measures=[...prompt.matchAll(/\b(?:a\s+)?([a-z]+)\s+(?:is|measures)\s+(\d+)\s*cm/gi)];
 var firstName=measures[0]?measures[0][1]:"الطول الأول",firstValue=measures[0]?Number(measures[0][2]):values[0];
 var secondName=measures[1]?measures[1][1]:"الطول الثاني",secondValue=measures[1]?Number(measures[1][2]):values[1];
 var max=Math.max(firstValue,secondValue),difference=/how much longer|how much shorter|what is the difference|how many cm more/i.test(prompt);
 return wrap(difference?"اكتشف الفرق بين الطولين":"قارن بين الطولين",'<div class="visual-measure-bars"><div><b>'+esc(firstName)+' · '+firstValue+' cm</b><i style="width:'+firstValue/max*100+'%"></i></div><div><b>'+esc(secondName)+' · '+secondValue+' cm</b><i style="width:'+secondValue/max*100+'%"></i></div></div>'+(difference?'<div class="visual-measure-difference"><span>'+firstValue+' cm</span><b>−</b><span>'+secondValue+' cm</span><b>= ؟</b></div>':"")+svg("ruler","مسطرة"),"measurement");
}


function comparisonItemVisual(item){
 var value=String(item==null?"":item),palette={"🟦":"#4d9fd3","🟨":"#efc44d","🟥":"#e66b6b","🟩":"#70b77a","🟪":"#9b83c7"};
 if(palette[value])return '<span class="visual-ribbon-block" role="img" aria-label="'+esc(value)+'" style="display:inline-block;width:28px;height:28px;border-radius:6px;border:2px solid rgba(30,55,75,.18);background:'+palette[value]+';margin:3px"></span>';
 var kind=kindOf(value);
 if(kind!=="generic")return svg(kind,value);
 return '<span class="visual-ribbon-block" role="img" aria-label="'+esc(value)+'" style="display:inline-block;min-width:28px;padding:4px;border-radius:6px;background:#e7edf3;margin:3px">'+esc(value)+'</span>';
}


function fractionScene(q,d,text){
 var prompt=String(q.prompt||text||""),pm=prompt.match(/(\d+)\s+equal parts/i),sm=prompt.match(/(\d+)\s+(?:is|are)\s+shaded/i),parts=Math.max(2,Math.min(8,Number(pm&&pm[1]||3))),shaded=Math.max(0,Math.min(parts,Number(sm&&sm[1]||1)));
 var cells=Array.from({length:parts},function(_,i){return '<span class="visual-fraction-part" style="display:inline-flex;align-items:center;justify-content:center;flex:1;height:48px;border:2px solid #64809a;border-radius:5px;background:'+(i<shaded?'#63a8d1':'#f7fbff')+'">'+(i<shaded?'●':'')+'</span>';}).join("");
 return wrap("الأجزاء المتساوية",svg("fraction","أجزاء متساوية")+'<div class="visual-fraction-parts" role="img" aria-label="'+shaded+' من '+parts+' أجزاء مظللة" style="display:flex;gap:4px;max-width:300px;margin:12px auto">'+cells+'</div>',"fraction");
}
function currencyScene(q,d,text){
 var prompt=String(q.prompt||text||""),amountMatches=Array.from(prompt.matchAll(/(\d+)\s+piasters/gi)),amount=Number(amountMatches.length?amountMatches[amountMatches.length-1][1]:200),rate=Number((prompt.match(/(\d+)\s+piasters?\s+make\s+one\s+pound/i)||[])[1]||100),coins=Math.max(1,Math.min(4,Math.ceil(amount/rate)));
 var coinVisual=Array.from({length:coins},function(){return svg("piaster","100 piasters");}).join("");
 return wrap("تحويل القروش إلى جنيهات",'<div class="visual-currency-conversion"><div class="visual-currency-coins">'+coinVisual+'<b>'+amount+' piasters</b></div><div class="visual-currency-arrow">→</div><div class="visual-currency-result">'+svg("pound","pound")+'<b>؟ pounds</b></div></div>',"currency");
}
function temperatureScene(q,d,text){
 return wrap("قياس درجة الحرارة",svg("thermometer","مقياس الحرارة")+'<div class="visual-temperature-scale" style="display:flex;justify-content:center;gap:20px;align-items:center;margin:12px auto"><span>بارد</span><div style="height:74px;width:14px;border:3px solid #67859a;border-radius:12px;position:relative"><i style="position:absolute;bottom:3px;left:3px;right:3px;height:28px;background:#e76f62;border-radius:8px"></i></div><span>حار</span></div>',"temperature");
}


function timeScene(q,d,text){
 var prompt=String(q.prompt||text||""),start=(prompt.match(/(\d{1,2}:\d{2})/)||[])[1]||"8:00",duration=(prompt.match(/one hour|1 hour/i)||[])[0]||"1 hour";
 return wrap("الوقت والمدة",svg("clock","الساعة")+'<div class="visual-time-equation" style="display:flex;justify-content:center;align-items:center;gap:10px;flex-wrap:wrap;margin:12px auto"><b>'+esc(start)+'</b><span>+</span><b>'+esc(duration)+'</b><span>=</span><b>؟</b></div>',"time");
}
function weightScene(q,d,text){
 var prompt=String(q.prompt||text||""),values=[...prompt.matchAll(/(\d+)\s*(kilograms?|grams?|kg|g)/gi)].map(m=>m[1]+" "+m[2]);
 if(values.length<2)values=["1 kilogram","500 grams"];
 return wrap("مقارنة الوزن",svg("scale","ميزان")+'<div class="visual-weight-values" style="display:flex;justify-content:center;gap:14px;flex-wrap:wrap;margin:12px auto"><b>'+esc(values[0])+'</b><span>مقابل</span><b>'+esc(values[1])+'</b></div>',"weight");
}

function render(q,d){
 q=q||{};d=d||q.legacyData||{};
 var type=q.type||d.type||"",text=getQuestionText(q,d),v=d.visual||{},html="",skill=String(q.skill||"");

 var promptLower=String(text||q.prompt||d.title||"").toLowerCase();
 if(/equal parts|fraction|shaded/.test(promptLower))return fractionScene(q,d,text);
 if(/piaster|pound/.test(promptLower))return currencyScene(q,d,text);
 if(/temperature|thermometer/.test(promptLower))return temperatureScene(q,d,text);
 if(q.id==="new-aboudy-maths-029"||/\d{1,2}:\d{2}|one hour|1 hour/.test(promptLower))return timeScene(q,d,text);
 if(q.id==="new-aboudy-maths-034"||/kilogram|grams|heavier/.test(promptLower))return weightScene(q,d,text);


 if(/\b\d+\s*cm\b/i.test(promptLower)){var measuredScene=measurementScene(q,d,text);if(measuredScene)return measuredScene;}

 if(q.id==="legacy-aboudy-0005"||(promptLower.includes("days of the week")&&promptLower.includes("capital letter")))return daysCapitalization();
 if(q.id==="legacy-aboudy-0252"||(promptLower.includes("supporting detail")&&promptLower.includes("main idea")))return mainIdeaSupport();
 if(q.id==="legacy-aboudy-0262"||(promptLower.includes("evidence from the text")&&promptLower.includes("inference")))return inferenceEvidence();

 if(type==="count")return countQuestion(q,d,text);
 if(type==="mc_text"||type==="mc_picture"){var mixedScene=mixedEntityProblem(q,d,text);if(mixedScene)return mixedScene;}
 if(type==="compare"&&(Array.isArray(d.groupA)||Array.isArray(d.groupB))){
  var a=(d.groupA||[]).map(function(x){return comparisonItemVisual(x);}).join("");
  var b=(d.groupB||[]).map(function(x){return comparisonItemVisual(x);}).join("");
  return wrap("قارن بين المجموعتين",'<div class="visual-compare-groups"><div><b>المجموعة A</b>'+a+'</div><div><b>المجموعة B</b>'+b+'</div></div>',"compare");
 }
 if(v.kind==="ten-frame"||v.kind==="ten_frame"){
  var total=Math.max(1,Math.min(20,Number(v.total)||10)),filled=Math.max(0,Math.min(total,Number(v.filled)||0));
  return wrap("إطار العشرة: الخانات الملوّنة والفارغة",'<div class="visual-ten-frame">'+Array.from({length:total}).map(function(_,i){return '<span class="'+(i<filled?"filled":"")+'"></span>';}).join("")+'</div><p class="visual-visual-label">'+esc(v.label||"عدّ الخانات الملوّنة والفارغة")+'</p>',"ten-frame");
 }
 if(v.kind==="array")return groupedArray(q,d,text);
 if(v.kind==="ruler")return rulerVisual(q,d);
 if(v.kind==="bar-model")return wordProblem(q,d,text);
 if(v.kind==="number_line")return numberPattern(q,text)||genericVisual(q,d,text);
 if(type==="word_problem")return wordProblem(q,d,text);
 if(type==="place_value")return placeValue(q,d,text)||genericVisual(q,d,text);
 if(type==="number_pattern"||type==="number_line")return numberPattern(q,text)||genericVisual(q,d,text);
 if(type==="graph")return graph(q,text)||genericVisual(q,d,text);
 if(type==="money")return money(q,d);
 if(type==="match_pairs")return matchPairs(q,d)||genericVisual(q,d,text);
 if(type==="reading_comprehension"||type==="comprehension")return reading(q,d,text);
 if(type==="clock")return wrap("ساعة مرسومة",'<div class="visual-clock-face">'+svg("clock","ساعة")+'</div><div class="visual-clock-label">'+esc(q.prompt||"اقرأ الساعة")+'</div>',"clock");
 if(type==="spelling"||type==="phonics"||type==="fill_gap"){
  var target=d.target||d.result||q.answer,kind=kindOf(target);
  if(type==="fill_gap"&&(kind==="generic"||kind==="letter"))kind=kindOf(d.hint||text);
  if(kind==="generic")kind=kindOf(d.hint||text);
  if(kind==="generic")kind="letter";
  var letters=Array.isArray(d.letters)?d.letters:[];
  return wrap("صورة تساعدك على فهم الكلمة",'<div class="visual-word-picture"><div class="visual-object-card">'+svg(kind,kind)+'</div></div>'+(letters.length?'<div class="visual-letter-tiles">'+letters.map(function(x){return '<span>'+esc(x)+'</span>';}).join("")+'</div>':""),"word-picture");
 }
 if(type==="tracing"){
  if(skill==="creative-drawing"||!String(d.letter||"").trim()){
   return wrap("مساحة تخيّل ورسم",'<div class="visual-drawing-prompt">'+card("canvas","لوحة الرسم")+card("pencil","قلم تلوين")+card("sun","شمس")+'</div>',"drawing");
  }
  var mark=String(d.letter||q.answer||"A"),markKind=/^\d+$/.test(mark)?"number":"letter";
  var related=kindOf(text);if(related==="generic")related=markKind==="number"?"star":"apple";
  return wrap("شاهد الرمز والصورة",'<div class="visual-tracing"><strong>'+esc(mark)+'</strong>'+card(related,related)+'</div>',"tracing");
 }
 if(type==="build_sentence"){
  var words=Array.isArray(d.words)?d.words:[],scene=sceneKinds(words.join(" "),3);
  return wrap("صورة تساعدك على تكوين الجملة",'<div class="visual-sentence-scene">'+(scene.length?scene.map(function(k){return card(k,k);}).join(""):sceneFromText(words.join(" "),skill))+'</div><div class="visual-word-strip">'+words.map(function(x){return '<span>'+esc(x)+'</span>';}).join("")+'</div>',"sentence");
 }
 if(type==="mc_picture")return wrap("شاهد الصور واختر ما يناسب السؤال",optionCards(q.options||d.options||[],q),"picture-choices");
 if(/science/i.test(skill)){var scienceVisual=scienceScene(q,d,text);if(scienceVisual)return scienceVisual;}
 if(type==="true_false"){
  if(/math/i.test(skill)){var mathVisual=mathConcept(q,d,text);if(mathVisual)return mathVisual;}
  var phonicsVisual=quotedWordScene(q,d,text);if(phonicsVisual)return phonicsVisual;
  var scene=promptScene(q,d,text);if(scene)return scene;
  return genericVisual(q,d,text);
 }
 if(type==="mc_text"){
  if(/pattern-master|find-the-difference/i.test(skill)){var colors=colorPattern(q,text);if(colors)return colors;}
  if(/math|number-trail|math-detective/i.test(skill)){var mathVisual=mathConcept(q,d,text);if(mathVisual)return mathVisual;}
  var scene=promptScene(q,d,text);if(scene)return scene;
 }
 if(type==="compare")return genericVisual(q,d,text);
 html=promptScene(q,d,text);
 if(html)return html;
 return genericVisual(q,d,text);
}
function semanticOptionKinds(q,label){
 var text=norm(label),prompt=String(q&&q.prompt||""),found=findKinds(label);
 if(/\b(never happens|does not happen|doesn't happen|do not need|does not need|don't need|doesn't need|not need|not true|false statement)\b/i.test(text)){
  var context=sceneKinds(prompt,2);return context.length?["negation",context[0]]:["negation"];
 }
 if(/\bforest\b/i.test(text))return ["tree","tree","tree"];
 if(/\bdesert\b/i.test(text))return ["camel","sun"];
 if(/\bmammal\b/i.test(text))return ["goat"];
 if(/\bbright|shine|shines|shining\b/i.test(text))return ["star"];
 if(/\bquickly|quick|fast\b/i.test(text))return ["person","wind"];
 if(/\brun|runs|running\b/i.test(text))return ["person","road"];
 if(/\bhop|hops|jump|jumps\b/i.test(text))return ["rabbit"];
 if(/\bplay|plays|playing\b/i.test(text))return ["person","ball"];
 if(/\braincoat\b/i.test(text))return ["raincoat","rain"];
 if(/\bsoap|germ|germs\b/i.test(text))return ["hand","soap","germ"];
 if(/\bglass\b/i.test(text))return ["glass","sun"];
 if(/\bskull\b/i.test(text))return ["skull","brain"];
 if(/\bbrain\b/i.test(text))return ["brain","skull"];
 if(/\bcork\b/i.test(text))return ["cork","water"];
 if(/\bstove\b/i.test(text))return ["stove","sun"];
 if(/\bdaylight|sunlight\b/i.test(text)){
  var daylightKinds=found.filter(function(kind){return kind!=="sun"||/\bsunlight|\bsun\b/i.test(text);});
  return daylightKinds.length?daylightKinds.slice(0,3):["sun"];
 }
 if(/\bair\b/i.test(text))return ["wind"];
 if(/\bocean|sea\b/i.test(text))return ["water","fish"];
 if(/\bbig|bigger|large|huge\b/i.test(text))return ["big"];
 if(/\bsmall|smaller|tiny|little\b/i.test(text))return ["small"];
 if(found.length)return found.slice(0,3);
 return [];
}

function wordCardVisual(label){
 var value=String(label||"").trim(),chars=Array.from(value).slice(0,18);
 var tiles=chars.map(function(ch){return ch===" "?'<span style="display:inline-block;width:8px"></span>':'<span style="display:inline-grid;place-items:center;min-width:27px;height:31px;margin:2px;background:#fff2d8;border:1px solid #e5c37b;border-radius:7px;font-weight:900;color:#76521e">'+esc(ch)+'</span>';}).join("");
 return '<div class="visual-word-option" role="img" aria-label="'+esc(value)+'"><div style="display:flex;flex-wrap:wrap;justify-content:center;align-items:center">'+tiles+'</div><b style="display:block;margin-top:5px">'+esc(value)+'</b></div>';
}

function optionVisualKind(q,value){
 q=q||{};var label=String(value==null?"":value).trim(),type=q.type||"",prompt=String(q.prompt||"");
 if(type==="true_false")return "truth";
 if(type==="compare"&&/^[AB]$/i.test(label))return "comparison";
 if(/^\d+$/.test(label))return countedEntities(prompt).length>=2?"mixed-count":"count";
 if(/^[A-Za-z]$/.test(label))return "letter";
 if(type==="clock"||/^\d{1,2}:\d{2}$/.test(label)||/\b(am|pm|o.clock)\b/i.test(label))return "clock";
 if(type==="money"&&/[$¢£]/.test(label))return "money";
 var kinds=semanticOptionKinds(q,label);
 if(kinds.length)return kinds[0]==="negation"?"negation":"semantic";
 return "word-card";
}
function renderOption(q,value){
 q=q||{};var d=q.legacyData||{},label=String(value==null?"":value).trim(),type=q.type||"",prompt=String(q.prompt||""),visualKind=optionVisualKind(q,label);
 if(type==="true_false"){
  var yes=/^(true|yes|correct)$/i.test(label),symbol=yes?'<path d="M25 50l16 16 35-38" fill="none" stroke="#16835f" stroke-width="9" stroke-linecap="round" stroke-linejoin="round"/>':'<path d="M30 30l40 40M70 30L30 70" fill="none" stroke="#bd4b43" stroke-width="9" stroke-linecap="round"/>';
  return '<div class="visual-choice-art visual-choice-symbol" data-option-visual-kind="truth"><svg class="visual-art" viewBox="0 0 100 96" role="img" aria-label="'+esc(label)+'"><circle cx="50" cy="48" r="38" fill="'+(yes?"#e6f7ef":"#fff0ee")+'" stroke="'+(yes?"#65b99b":"#e49b91")+'" stroke-width="3"/>'+symbol+'</svg></div>';
 }
 if(type==="compare"&&/^[AB]$/i.test(label)){
  var chosen=label.toUpperCase()==="A"?(d.groupA||[]):(d.groupB||[]);
  return '<div class="visual-choice-art visual-choice-group" data-option-visual-kind="comparison">'+chosen.map(function(x){var item=String(x&&x.name||x);return comparisonItemVisual(item);}).join("")+'<b>Group '+label.toUpperCase()+'</b></div>';
 }
 if(/^\d{1,2}:\d{2}$/.test(label))return '<div class="visual-choice-art" data-option-visual-kind="clock">'+svg("clock",label)+'<b>'+esc(label)+'</b></div>';
 if(/^\d+$/.test(label)){
  var n=Math.max(0,Math.min(20,Number(label))),entities=countedEntities(prompt),item=kindOf(d.item||"");
  if(entities.length>=2&&/\b(plus|add|altogether|in all|total|together|combined|and)\b/i.test(prompt)){
   return '<div class="visual-choice-art visual-choice-count visual-choice-mixed" data-option-visual-kind="mixed-count">'+entityGrid(entities[0])+entityGrid(entities[1])+'<b>'+esc(label)+'</b></div>';
  }
  if(item==="generic"&&entities.length)item=entities[0].kind;
  if(item==="generic")item=kindOf(prompt);
  if(item!=="generic"&&n>0)return '<div class="visual-choice-art visual-choice-count" data-option-visual-kind="count">'+objectGrid(item,n,item)+'<b>'+esc(label)+'</b></div>';
  return '<div class="visual-choice-art" data-option-visual-kind="number">'+svg("number",label)+'</div>';
 }
 if(/^[A-Za-z]$/.test(label)){
  var l=label.toUpperCase(),example=LETTER_WORDS[l]||"letter",exampleKind=kindOf(example);
  return '<div class="visual-choice-art visual-choice-letter" data-option-visual-kind="letter">'+svg("letter",l)+(exampleKind!=="generic"?svg(exampleKind,example):"")+'</div>';
 }
 if(type==="clock"||/\b(am|pm|o.clock)\b/i.test(label))return '<div class="visual-choice-art" data-option-visual-kind="clock">'+svg("clock",label)+'</div>';
 if(type==="money"&&/[$¢£]/.test(label))return '<div class="visual-choice-art" data-option-visual-kind="money">'+svg("coin",label)+'</div>';
 var kinds=semanticOptionKinds(q,label);
 if(kinds.length){
  if(kinds[0]==="negation"){
   var context=kinds[1]||sceneKinds(prompt,1)[0]||"book";
   return '<div class="visual-choice-art visual-choice-negation" data-option-visual-kind="negation">'+svg(context,context)+'<svg class="visual-art" viewBox="0 0 100 96" role="img" aria-label="not"><circle cx="50" cy="48" r="39" fill="none" stroke="#d44e4e" stroke-width="7"/><path d="M22 20l56 56" stroke="#d44e4e" stroke-width="8" stroke-linecap="round"/></svg><span>'+esc(label)+'</span></div>';
  }
  if(kinds[0]==="big"||kinds[0]==="small")return '<div class="visual-choice-art" data-option-visual-kind="concept">'+svg(kinds[0],label)+'</div>';
  var color=findKinds(label).find(function(k){return /^(red|blue|green|yellow|purple|white|black|brown|grey|gray|pink|orange)$/.test(k);});
  var object=findKinds(label).find(function(k){return !/^(red|blue|green|yellow|purple|white|black|brown|grey|gray|pink|orange)$/.test(k);});
  if(color&&object==="ball")return '<div class="visual-choice-art" data-option-visual-kind="semantic">'+coloredObjectGrid("ball",1,color)+'</div>';
  return '<div class="visual-choice-art" data-option-visual-kind="semantic">'+kinds.slice(0,3).map(function(k){return svg(k,k);}).join("")+'<span>'+esc(label)+'</span></div>';
 }
 return '<div class="visual-choice-art" data-option-visual-kind="word-card">'+wordCardVisual(label)+'</div>';
}
root.ScholarsVisuals={version:"1.9.0",render:render,renderOption:renderOption,optionVisualKind:optionVisualKind,kindOf:kindOf,svg:svg,findKinds:findKinds,countedEntities:countedEntities,visualKinds:Object.keys(TERMS),hasSpecificVisual:hasSpecificVisual};
})(window);
